#import pytest
import unittest
from flask import Response, json
from swapi import app_factory


#@pytest.fixture
def client():
    app = app_factory.create_app()
    app.config['TESTING'] = True
    return app.test_client()


class SwapiEndpointsTest(unittest.TestCase):

    def setUp(self) -> None:
        super().setUp()
        self.client = client()

    def test_get_root_path(self):
        resp: Response = self.client.get('/')
        self.assertEqual(resp.status_code, 404, 'should return not_found on root path')

    def test_get_endpoint_listing(self):
        resp: Response = self.client.get('/swapi/')
        self.assertEqual(resp.status_code, 200, 'status code should be OK')
        res_links = json.loads(resp.data)
        self.assertGreater(len(res_links), 0, 'should contain links')
        self.assertEqual(res_links['films'], '/swapi/films/', 'should contain link to films')

    def test_get_person(self):
        resp: Response = self.client.get('/swapi/persons/cj0nv9pdlewd80130bs5vgryn')
        self.assertEqual(resp.status_code, 200, 'status code should be OK')
        person = json.loads(resp.data)
        self.assertEqual(person['name'], 'Han Solo')
        self.assertEqual(len(person['films']), 4)
        for film_uri in person['films']:
            self.assertRegex(film_uri, '/swapi/films/', 'film link should start with correct path')
        self.assertEqual(person['homeworld'], '/swapi/planets/cj0o7m38ws0py0172st3yhqvj')


if __name__ == '__main__':
    unittest.main()
