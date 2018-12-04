(function() {
    'use strict';

    angular
        .module('hackforgoodApp')
        .controller('SurveyController', SurveyController);

    SurveyController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Questions', 'Results'];

    function SurveyController ($scope, Principal, LoginService, $state, Questions, Results) {
        var vm = this;
        var mapping = {};
        vm.questions = [];
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Questions.query(function(result) {
                vm.questions = result;
                vm.searchQuery = null;
                convertTojson();
            });
        }

        function convertTojson() {
            var json = {
                title: "Feedback",
                showProgressBar: "top",
                pages: [{questions:[]}]
            };
            angular.forEach(vm.questions,function (data) {
                if(data.type) {


                    var element = {"id":data["id"],"type": data["type"], "name": data["name"], "title": data["title"]};
                    mapping[data["name"]] = data["id"];
                    if (data.type == "radiogroup") {
                        var choices = [];
                        if(data.choice_x)
                            choices.push(data.choice_x);
                        if(data.choice_y)
                            choices.push(data.choice_y);
                        if(data.choice_z)
                            choices.push(data.choice_z);

                        element["choices"] = choices;
                    }
                    json.pages[0].questions.push(element);
                }
            });
            console.log(json);

            window.survey = new Survey.Model(json);
            window.survey.onComplete.add(function(result) {
                console.log("result",result.data);
                angular.forEach(result.data,function (value,key) {
                   console.log(key,value,mapping[key]);
                });
                document.querySelector("#surveyResult").innerHTML =
                    "result: " + JSON.stringify(result.data);
            });

            $("#surveyElement").Survey({
                model: window.survey
            });
        }
        // function init() {
        //     var json = {
        //         title: "Product Feedback Survey Example",
        //         showProgressBar: "top",
        //         pages: [
        //             {
        //                 questions: [
        //                     {
        //                         type: "matrix",
        //                         name: "Quality",
        //                         title:
        //                             "Please indicate if you agree or disagree with the following statements",
        //                         columns: [
        //                             { value: 1, text: "Strongly Disagree" },
        //                             { value: 2, text: "Disagree" },
        //                             { value: 3, text: "Neutral" },
        //                             { value: 4, text: "Agree" },
        //                             { value: 5, text: "Strongly Agree" }
        //                         ],
        //                         rows: [
        //                             { value: "affordable", text: "Product is affordable" },
        //                             {
        //                                 value: "does what it claims",
        //                                 text: "Product does what it claims"
        //                             },
        //                             {
        //                                 value: "better then others",
        //                                 text: "Product is better than other products on the market"
        //                             },
        //                             { value: "easy to use", text: "Product is easy to use" }
        //                         ]
        //                     },
        //                     {
        //                         type: "rating",
        //                         name: "satisfaction",
        //                         title: "How satisfied are you with the Product?",
        //                         mininumRateDescription: "Not Satisfied",
        //                         maximumRateDescription: "Completely satisfied"
        //                     },
        //                     {
        //                         type: "rating",
        //                         name: "recommend friends",
        //                         visibleIf: "{satisfaction} > 3",
        //                         title:
        //                             "How likely are you to recommend the Product to a friend or co-worker?",
        //                         mininumRateDescription: "Will not recommend",
        //                         maximumRateDescription: "I will recommend"
        //                     },
        //                     {
        //                         type: "comment",
        //                         name: "suggestions",
        //                         title: "What would make you more satisfied with the Product?"
        //                     }
        //                 ]
        //             },
        //             {
        //                 questions: [
        //                     {
        //                         type: "radiogroup",
        //                         name: "price to competitors",
        //                         title: "Compared to our competitors, do you feel the Product is",
        //                         choices: [
        //                             "Less expensive",
        //                             "Priced about the same",
        //                             "More expensive",
        //                             "Not sure"
        //                         ]
        //                     },
        //                     {
        //                         type: "radiogroup",
        //                         name: "price",
        //                         title: "Do you feel our current price is merited by our product?",
        //                         choices: [
        //                             "correct|Yes, the price is about right",
        //                             "low|No, the price is too low for your product",
        //                             "high|No, the price is too high for your product"
        //                         ]
        //                     },
        //                     {
        //                         type: "multipletext",
        //                         name: "pricelimit",
        //                         title: "What is the... ",
        //                         items: [
        //                             {
        //                                 name: "mostamount",
        //                                 title: "Most amount you would every pay for a product like ours"
        //                             },
        //                             {
        //                                 name: "leastamount",
        //                                 title: "The least amount you would feel comfortable paying"
        //                             }
        //                         ]
        //                     }
        //                 ]
        //             },
        //             {
        //                 questions: [
        //                     {
        //                         type: "text",
        //                         name: "email",
        //                         title:
        //                             "Thank you for taking our survey. Your survey is almost complete, please enter your email address in the box below if you wish to participate in our drawing, then press the 'Submit' button."
        //                     }
        //                 ]
        //             }
        //         ]
        //     };
        //
        //     window.survey = new Survey.Model(json);
        //     survey.onComplete.add(function(result) {
        //         document.querySelector("#surveyResult").innerHTML =
        //             "result: " + JSON.stringify(result.data);
        //     });
        //
        //     $("#surveyElement").Survey({
        //         model: survey
        //     });
        // }

        // if (!window["%hammerhead%"]) {
        //     init();
        // }
    }
})();
