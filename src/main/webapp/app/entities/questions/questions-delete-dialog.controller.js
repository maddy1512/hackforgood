(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('QuestionsDeleteController',QuestionsDeleteController);

    QuestionsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Questions'];

    function QuestionsDeleteController($uibModalInstance, entity, Questions) {
        var vm = this;

        vm.questions = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Questions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
