(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('QuestionsDetailController', QuestionsDetailController);

    QuestionsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Questions'];

    function QuestionsDetailController($scope, $rootScope, $stateParams, previousState, entity, Questions) {
        var vm = this;

        vm.questions = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackforgoodApp:questionsUpdate', function(event, result) {
            vm.questions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
