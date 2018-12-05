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
        $scope.chartData = {};
        $scope.items = ["All","Yerwada","Warje","Tingrenagar"];
        $scope.filter = "All";
        vm.questions = [];
        var ommitted = [5,7,8];
        vm.loadAll = loadAll;
        loadAll();
        function loadAll() {
            Results.getChartData({filter:$scope.filter},function (results) {

                var charts = {};
                angular.forEach(results, function (chartdata) {
                    if(ommitted.indexOf(chartdata.questionId) === -1) {
                        var qid = chartdata.questionId;
                        var result = chartdata.result;
                        var count = chartdata.count;
                        var title = chartdata.title;
                        if (charts.hasOwnProperty(qid)) {
                            charts[qid]["data"].push(count);
                            charts[qid]["labels"].push(result);
                        } else {
                            charts[qid] = {"data": [count], "labels": [result], "title": title, "colors" : [  '#008000', '#e39538', '#fd0017'] };
                        }
                    }
                });
                $scope.chartData = charts;
            });
        }

        $scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
        $scope.data = [300, 500, 100];

    }
})();
