(function() {
    'use strict';
    angular
        .module('hackforgoodApp')
        .factory('Results', Results);

    Results.$inject = ['$resource', 'DateUtils'];

    function Results ($resource, DateUtils) {
        var resourceUrl =  'api/results/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.survey_timestamp = DateUtils.convertLocalDateFromServer(data.survey_timestamp);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.survey_timestamp = DateUtils.convertLocalDateToServer(copy.survey_timestamp);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.survey_timestamp = DateUtils.convertLocalDateToServer(copy.survey_timestamp);
                    return angular.toJson(copy);
                }
            },
            'saveAll':{
                url:'api/resultsall/',
                method:'POST',
                isArray:true
            },
            'getChartData':{
                url:'api/chart/:filter',
                method:'GET',
                isArray:true,
                params: {
                    filter: '@filter'
                }
            }
        });
    }
})();
