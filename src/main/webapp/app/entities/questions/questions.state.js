(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('questions', {
            parent: 'entity',
            url: '/questions',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackforgoodApp.questions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/questions/questions.html',
                    controller: 'QuestionsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('questions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('questions-detail', {
            parent: 'questions',
            url: '/questions/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackforgoodApp.questions.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/questions/questions-detail.html',
                    controller: 'QuestionsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('questions');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Questions', function($stateParams, Questions) {
                    return Questions.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'questions',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('questions-detail.edit', {
            parent: 'questions-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/questions/questions-dialog.html',
                    controller: 'QuestionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Questions', function(Questions) {
                            return Questions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('questions.new', {
            parent: 'questions',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/questions/questions-dialog.html',
                    controller: 'QuestionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                name: null,
                                title: null,
                                choice_x: null,
                                choice_y: null,
                                choice_z: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('questions', null, { reload: 'questions' });
                }, function() {
                    $state.go('questions');
                });
            }]
        })
        .state('questions.edit', {
            parent: 'questions',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/questions/questions-dialog.html',
                    controller: 'QuestionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Questions', function(Questions) {
                            return Questions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('questions', null, { reload: 'questions' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('questions.delete', {
            parent: 'questions',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/questions/questions-delete-dialog.html',
                    controller: 'QuestionsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Questions', function(Questions) {
                            return Questions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('questions', null, { reload: 'questions' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
