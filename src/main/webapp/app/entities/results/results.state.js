(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('results', {
            parent: 'entity',
            url: '/results',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackforgoodApp.results.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/results/results.html',
                    controller: 'ResultsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('results');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('results-detail', {
            parent: 'results',
            url: '/results/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackforgoodApp.results.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/results/results-detail.html',
                    controller: 'ResultsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('results');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Results', function($stateParams, Results) {
                    return Results.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'results',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('results-detail.edit', {
            parent: 'results-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/results/results-dialog.html',
                    controller: 'ResultsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Results', function(Results) {
                            return Results.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('results.new', {
            parent: 'results',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/results/results-dialog.html',
                    controller: 'ResultsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                user: null,
                                survey_id: null,
                                question_id: null,
                                question_name: null,
                                question_result: null,
                                survey_timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('results', null, { reload: 'results' });
                }, function() {
                    $state.go('results');
                });
            }]
        })
        .state('results.edit', {
            parent: 'results',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/results/results-dialog.html',
                    controller: 'ResultsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Results', function(Results) {
                            return Results.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('results', null, { reload: 'results' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('results.delete', {
            parent: 'results',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/results/results-delete-dialog.html',
                    controller: 'ResultsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Results', function(Results) {
                            return Results.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('results', null, { reload: 'results' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
