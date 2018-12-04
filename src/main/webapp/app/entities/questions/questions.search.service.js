(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .factory('QuestionsSearch', QuestionsSearch);

    QuestionsSearch.$inject = ['$resource'];

    function QuestionsSearch($resource) {
        var resourceUrl =  'api/_search/questions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
