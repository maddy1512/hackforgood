(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('ResultsController', ResultsController);

    ResultsController.$inject = ['$scope', '$state', 'Results', 'ResultsSearch'];

    function ResultsController ($scope, $state, Results, ResultsSearch) {
        var vm = this;

        vm.results = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Results.query(function(result) {
                vm.results = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ResultsSearch.query({query: vm.searchQuery}, function(result) {
                vm.results = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
