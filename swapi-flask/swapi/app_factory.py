from flask import Flask
import logging


def create_app():
    """flask application factory"""
    logging.basicConfig(level=logging.DEBUG)
    app = Flask(__name__, instance_relative_config=True)
    from . import swapi_controller
    app.register_blueprint(swapi_controller.blueprint)
    return app

