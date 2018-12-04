(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Questions', 'Results'];

    function DashboardController ($scope, Principal, LoginService, $state, Questions, Results) {
        var vm = this;
        var mapping = {};
        var mappingQuestionName = {};

        vm.questions = [];
        vm.loadAll = loadAll;
        loadAll();
        function loadAll() {
            Results.getChartData().then(function (results) {
                $scope.chartData = [];
                var charts = {}
                angular.forEach(results, function (chartdata) {
                    var qid = chartdata.questionId;
                    var result = chartdata.result;
                    var count = chartdata.count;
                    if(charts.hasOwnProperty(qid)) {
                        charts[qid][result] =
                    }
                })
            })
        }

        $scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
        $scope.data = [300, 500, 100];

    }
})();
