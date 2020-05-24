class SwapiRelation:
    """
    Relation between two entities:
    attribute_name (=source) in owner of association and the referenced resource_set (=target)
    """
    def __init__(self, source, target, is_multiple):
        self.source = source
        self.target = target
        self.is_multiple = is_multiple


def single(source, target=None):
    t = target if target is not None else source
    return SwapiRelation(source, t, is_multiple=False)


def multi(source, target=None):
    t = target if target is not None else source
    return SwapiRelation(source, t, is_multiple=True)


film_relations = [multi('characters', 'persons'), multi('planets'),
                  multi('species'), multi('starships'), multi('vehicles')]

person_relations = [multi('films'), single('homeworld', 'planets'),
                    multi('species'), multi('starships'), multi('vehicles')]

planet_relations = [multi('films'), multi('residents', 'persons')]

species_relations = [multi('films'), multi('people', 'persons')]

starship_relations = [multi('films'), multi('pilots', 'persons')]

vehicle_relations = [multi('films'), multi('pilots', 'persons')]

swapi_relations = {'films': film_relations, 'persons': person_relations,
                   'planets': planet_relations, 'species': species_relations,
                   'starships': starship_relations, 'vehicles': vehicle_relations}
