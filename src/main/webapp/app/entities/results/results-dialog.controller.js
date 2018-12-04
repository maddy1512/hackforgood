(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('ResultsDialogController', ResultsDialogController);

    ResultsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Results'];

    function ResultsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Results) {
        var vm = this;

        vm.results = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.results.id !== null) {
                Results.update(vm.results, onSaveSuccess, onSaveError);
            } else {
                Results.save(vm.results, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackforgoodApp:resultsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.survey_timestamp = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
