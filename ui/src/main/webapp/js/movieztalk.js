var moviezApp=angular.module('movieztalk', ["ngRoute"]);
  
moviezApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'Movie.html',
        controller: 'LoadHomePageCont'
      }).
      when('/menu1', {
        templateUrl: 'test.html'
      }).
      when('/movie/:movieid', {
        templateUrl: 'Movie.html',
        controller: 'LoadMoviePageCont'
      }).
      when('/movie', {
          templateUrl: 'Movie.html',
          controller: 'LoadMoviePageCont'
        }).
     otherwise({
        redirectTo: '/home'
      });
  }]);


 moviezApp.controller("LoadMoviePageCont",function($scope,$routeParams,$http)
 { 

    $http({
          method: 'GET',
          url: '/ui/user',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.moviename=data;
          });

 });

 moviezApp.controller("LoadHomePageCont",function($scope,$routeParams,$http)
 {
    $http({
          method: 'GET',
          url: '/ui/carousel',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.movies=data; 
          });

    $http({
          method: 'GET',
          url: '/ui/topmovies',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.topmovies=data; 
          });
 });

 


moviezApp.directive('indicatorWidget', [function (){
  return {
    restrict: 'A',
    replace: true,
    transclude: true,
    controller: function($scope, $element, $attrs){
        var diff = ($scope.expected - $scope.actual)/$scope.expected,
            canvasWidth = $element.attr('width'),
            canvasHeight = $element.attr('height'),
            circle = $element.find('circle')[0],
            radius = circle.r.baseVal.value;

        $scope.radius = radius;
        $scope.canvasWidth = canvasWidth;
        $scope.canvasHeight = canvasHeight;
        $scope.spacing = 0.9;
        
        function convertToRads(angle){
            return angle * (Math.PI / 180);
        }

        function findDegress(percentage){
            return 360 * percentage;
        }

        function getArcValues(index, radius, spacing){
            return {
                innerRadius: (index + spacing) * radius,
                outerRadius: (index + spacing) * radius
            };
        }
        

        $scope.buildArc = function(){
            return d3
                    .svg
                    .arc()
                    .innerRadius(function(d){
                        return d.innerRadius;
                    })
                    .outerRadius(function(d){
                        return d.outerRadius;
                    })
                    .startAngle(0)
                    .endAngle(function(d){
                        return d.endAngle;
                    });
        };

        $scope.findPathColor = function(){
            return (diff < 0.25) ? 'all-good' :
                    ((diff >= 0.25 && diff < 0.5) ? 'not-so-good' :
                    'way-behind');
        };

        $scope.getArcInfo = function(index, value, radius, spacing){
            var end = findDegress(value),
                arcValues = getArcValues(index, radius, spacing);


            return {
                innerRadius: arcValues.innerRadius,
                outerRadius: arcValues.outerRadius,
                endAngle: convertToRads(end),
                startAngle: 0
            };
        };

        $scope.tweenArc = function(b, arc){
            return function(a) {
                var i = d3.interpolate(a, b);
                for(var key in b){
                    a[key] = b[key];
                }
                return function(t) {
                    return arc(i(t));
                };
            };
        }
    },
    templateUrl: 'indicator.html',
    link: function(scope, element, attrs){
        scope.actual_formatted = (scope.actual * 100).toFixed(0);
    },
    scope: {
        actual: '@',
        expected: '@'
    }
  };
  }]);

moviezApp.directive('pathGroup', function(){
  return {
    requires: '^indicatorWidget',
    link: function(scope, element, attrs, ctrl){
        element
            .attr(
                "transform", 
                "translate("+ scope.canvasWidth/2 + "," + scope.canvasHeight/2 + ")"
            );
    }
  };
  });

moviezApp.directive('innerPath', function(){
  return {
    restrict: 'A',
    transclude: true,
    requires: '^pathGroup',
    link: function(scope, element, attrs, ctrl){
        var arc = d3.select(element[0]),
            arcObject = scope.buildArc(),
            innerArc = scope.getArcInfo(1.1, scope.expected, scope.radius, 0.05),
            end = innerArc.endAngle,
            color = (scope.diff < 0.25) ? 'all-good' :
                    ((scope.diff >= 0.25 && scope.diff < 0.5) ? 'not-so-good' :
                    'way-behind');
    

        
        innerArc.endAngle = 0;
        arc
            .datum(innerArc)
            .attr('d', arcObject)
            .transition()
            .delay(100)
            .duration(2000)
            .attrTween("d", scope.tweenArc({
                endAngle: end
            }, arcObject));
    }
  };
  });

moviezApp.directive('outerPath', function(){
  return {
    restrict: 'A',
    transclude: true,
    requires: '^pathGroup',
    link: function(scope, element, attrs){
        var arc = d3.select(element[0]),
            arcObject = scope.buildArc(),
            outerArc = scope.getArcInfo(1.2, scope.actual, scope.radius, 0.1),
            end = outerArc.endAngle;
        
        outerArc.endAngle = 0;
        arc
            .datum(outerArc)
            .attr('d', arcObject)
            .transition()
            .delay(200)
            .duration(2500)
            .attrTween("d", scope.tweenArc({
                endAngle: end
            }, arcObject));


        element.addClass(scope.findPathColor());
    }
  }; 
 });


 moviezApp.controller('RatingCtrl', function($scope) {
    $scope.rating = 5;
    $scope.rateFunction = function(rating) {
      alert('Rating selected - ' + rating);
    };
  })
  .directive('starRating',
  function() {
    return {
      restrict : 'A',
      template : '<ul class="rating">'
           + '  <li ng-repeat="star in stars" ng-class="star" ng-click="toggle($index)">'
           + '  <i class="fa fa-star fa-lg"> </i> '
           + '</li>'
           + '</ul>',
      scope : {
        ratingValue : '=',
        max : '=',
        onRatingSelected : '&',
      },
      link : function(scope, elem, attrs) {
        var updateStars = function() {
          scope.stars = [];
          for ( var i = 0; i < scope.max; i++) {
            scope.stars.push({
              filled : i < scope.ratingValue
            });
          }
        };
        
        scope.toggle = function(index) {
          scope.ratingValue = index + 1;
          scope.onRatingSelected({
            rating : index + 1
          });
        };

        scope.toggle = function(index) {
          scope.ratingValue = index + 1;
          scope.onRatingSelected2({
            rating : index + 1
          });
        };
        
        scope.$watch('ratingValue',
          function(oldVal, newVal) {
            if (newVal) {
              updateStars();
            }
          }
        );
      }
    };
  }
);