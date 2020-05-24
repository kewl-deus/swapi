import connexion
import logging


def main():
    logging.basicConfig(level=logging.DEBUG)
    app = connexion.App(__name__, specification_dir='./api/', options={"swagger_ui": True})
    #app.add_api('swapi-oa3.yaml', arguments={'title': 'SWAPI OpenAPI 3'})
    app.add_api('swapi-oa3.yaml',
                arguments={'title': 'SWAPI OpenAPI 3'},
                strict_validation=False,
                validate_responses=False,
                resolver=resolve_swapi_function
                )
    app.run(port=5000, debug=True)


def resolve_swapi_function(function_name):
    from . import swapi_controller

    swapi_functions = {'index': swapi_controller.index,
                       'get_all': swapi_controller.get_all,
                       'get_single': swapi_controller.get_single}

    return swapi_functions



if __name__ == '__main__':
    main()
