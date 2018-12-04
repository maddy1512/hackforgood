(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('ResultsDeleteController',ResultsDeleteController);

    ResultsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Results'];

    function ResultsDeleteController($uibModalInstance, entity, Results) {
        var vm = this;

        vm.results = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Results.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
