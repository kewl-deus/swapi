from flask import Flask


def create_app():
    """flask application factory"""
    app = Flask(__name__, instance_relative_config=True)
    from . import swapi
    app.register_blueprint(swapi.bp)
    return app




