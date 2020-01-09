import unittest
from __init__.logic.swapi_relations import swapi_relations, SwapiRelation


class SwapiRelationsTest(unittest.TestCase):
    def test_person_relations(self):
        person_rels = swapi_relations['persons']
        self.assertIsNotNone(person_rels, "person relations not found")

        person_homeworld = next(filter(lambda rel: rel.source == 'homeworld', person_rels))
        self.assertIsInstance(person_homeworld, SwapiRelation, "not of swapi relation type")
        self.assertFalse(person_homeworld.is_multiple, "single expected")
        self.assertEqual(person_homeworld.target, "planets", "target should be planets")


if __name__ == '__main__':
    unittest.main()
