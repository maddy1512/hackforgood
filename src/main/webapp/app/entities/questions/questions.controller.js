(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('QuestionsController', QuestionsController);

    QuestionsController.$inject = ['$scope', '$state', 'Questions', 'QuestionsSearch'];

    function QuestionsController ($scope, $state, Questions, QuestionsSearch) {
        var vm = this;

        vm.questions = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Questions.query(function(result) {
                vm.questions = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            QuestionsSearch.query({query: vm.searchQuery}, function(result) {
                vm.questions = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
