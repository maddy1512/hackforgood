(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('ResultsDetailController', ResultsDetailController);

    ResultsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Results'];

    function ResultsDetailController($scope, $rootScope, $stateParams, previousState, entity, Results) {
        var vm = this;

        vm.results = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackforgoodApp:resultsUpdate', function(event, result) {
            vm.results = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
