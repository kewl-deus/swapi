from flask import url_for, jsonify, json, abort, Response, Blueprint, current_app, request
from .logic.swapi_relations import swapi_relations

blueprint = Blueprint('swapi', __name__, url_prefix='/swapi')

res_names = ['films', 'persons', 'planets', 'species', 'starships', 'vehicles']


@blueprint.route('/', methods=['GET'])
def index():
    """Lists all available SWAPI resource endpoints"""
    endpoints = dict(map(lambda r: [r, __url_for(get_all, resource_name=r)], res_names))
    return endpoints


@blueprint.route('/<resource_name>/', methods=['GET'])
def get_all(resource_name):
    validate_resource_name(resource_name)
    if len(request.args) > 0:
        return search_resources(resource_name, request.args)

    json_entities = load_resource(resource_name)
    linkified_entities = list(map(lambda e: linkify(resource_name, e), json_entities))
    return jsonify(linkified_entities)


@blueprint.route('/<resource_name>/<resource_id>', methods=['GET'])
def get_single(resource_name, resource_id):
    validate_resource_name(resource_name)
    res_entity = find_resource(resource_name, resource_id)
    linkify(resource_name, res_entity)
    return res_entity


def search_resources(resource_name: str, searched_props: dict) -> bool:
    def matches_search_criteria(json_obj: dict):
        for (prop_name, prop_value) in searched_props.items():
            if prop_name in json_obj:
                if str(json_obj[prop_name]) != str(prop_value):
                    return False
            else:
                return False
        return True

    def to_str(prop):
        k, v = prop
        return f'{k}={v}'

    res_json = load_resource(resource_name)
    matching_objs = list(filter(matches_search_criteria, res_json))
    if len(matching_objs) == 0:
        prop_str_list = list(map(to_str, searched_props.items()))
        search_exp = str.join(",", prop_str_list)
        abort(Response(f'No resource found with {search_exp} in {resource_name}', 404))
    #name = request.args.get('name') #if key doesn't exist, returns None
    return jsonify(matching_objs)


def find_resource(resource_name, resource_id):
    res_json = load_resource(resource_name)
    res_item = next(filter(lambda r: r['id'] == resource_id, res_json))
    if res_item is not None:
        return res_item
    abort(Response(f'No resource found with id={resource_id} in {resource_name}', 404))


def validate_resource_name(resource_name):
    if resource_name not in res_names:
        res_names_formatted = list(map(lambda r: f"'{r}'", res_names))
        res_names_csv = str.join(', ', res_names_formatted)
        abort(Response(f'Resource \'{resource_name}\' not found. Use one of {res_names_csv}', 404))


def load_resource(resource_name):
    current_app.logger.info(f'Loading resource {resource_name}.json')
    with open(f'{blueprint.root_path}/static/data/{resource_name}.json', 'r') as res_file:
        res_json = json.load(res_file)
        return res_json


def linkify(res_name, res_entity):
    """
    Converts IDs of related entities to URIs.
    The res_entity object is modified within this process.
    """

    relations = swapi_relations[res_name]
    for rel in relations:

        def url_for_single(ref_json, resource_name=rel.target):
            if ref_json is None:
                return None
            return __url_for(get_single, resource_name=resource_name, resource_id=ref_json['id'])

        if rel.is_multiple:
            ref_list = res_entity[rel.source]  # array of json objects {'id':'abc123'}
            link_list = list(map(url_for_single, ref_list))
            res_entity[rel.source] = link_list
        else:
            ref = res_entity[rel.source]
            link = url_for_single(ref)
            res_entity[rel.source] = link
    return res_entity


def __url_for(controller_method, **values):
    """internal version of url_for"""
    return url_for(f'{blueprint.name}.{controller_method.__name__}', **values)
