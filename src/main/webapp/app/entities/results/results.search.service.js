(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .factory('ResultsSearch', ResultsSearch);

    ResultsSearch.$inject = ['$resource'];

    function ResultsSearch($resource) {
        var resourceUrl =  'api/_search/results/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
