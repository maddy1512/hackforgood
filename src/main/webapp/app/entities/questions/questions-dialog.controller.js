(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('QuestionsDialogController', QuestionsDialogController);

    QuestionsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Questions'];

    function QuestionsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Questions) {
        var vm = this;

        vm.questions = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.questions.id !== null) {
                Questions.update(vm.questions, onSaveSuccess, onSaveError);
            } else {
                Questions.save(vm.questions, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackforgoodApp:questionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
