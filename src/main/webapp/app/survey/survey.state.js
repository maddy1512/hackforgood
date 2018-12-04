(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('survey', {
            parent: 'app',
            url: '/survey',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/survey/survey.html',
                    controller: 'SurveyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
