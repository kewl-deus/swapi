from flask import Flask, escape, url_for, jsonify, json, abort, Response

app = Flask(__name__)

res_names = ['films', 'persons', 'planets', 'species', 'starships', 'vehicles']

@app.route('/swapi', methods=['GET'])
def index():
    """Lists all available SWAPI resource endpoints"""
    endpoints = dict(map(lambda r: [r, url_for(get_all.__name__, resource_name=r)], res_names))
    return endpoints


@app.route('/swapi/<resource_name>/', methods=['GET'])
def get_all(resource_name):
    validate_resource_name(resource_name)
    return jsonify(load_resource(resource_name))


@app.route('/swapi/<resource_name>/<resource_id>', methods=['GET'])
def get_single(resource_name, resource_id):
    validate_resource_name(resource_name)

    res_json = load_resource(resource_name)
    for res_item in res_json:
        if res_item['id'] == resource_id:
            return res_item

    abort(Response(f'No item found with id={resource_id} in {resource_name}', 404))

    #res_items = list(filter(lambda r: r['id'] == resource_id, res_json))
    #if len(res_items) == 1:
    #    item = res_items.pop()
    #    return jsonify(item)
    #else:
    #    abort(Response(f'No item found with id={resource_id} in {resource_name}', 404))


def validate_resource_name(resource_name):
    if resource_name not in res_names:
        res_names_formatted = list(map(lambda r: f"'{r}'", res_names))
        res_names_csv = str.join(', ', res_names_formatted)
        abort(Response(f'Resource \'{resource_name}\' not found. Use one of {res_names_csv}', 404))


def load_resource(resource_name):
    app.logger.info(f'Loading resource {resource_name}.json')
    with open(f'static/data/{resource_name}.json', 'r') as res_file:
        res_json = json.load(res_file)
        return res_json