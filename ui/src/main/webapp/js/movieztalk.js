var moviezApp=angular.module('movieztalk', ["ngRoute", "ngAnimate","ui.bootstrap"]);
  
moviezApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'Home.html',
        controller: 'LoadHomePageCont'
      }).
      when('/menu1', {
        templateUrl: 'test.html'
      }).
      when('/movie/:movieid', {
        templateUrl: 'Movie.html',
        controller: 'LoadMoviePageCont'
      }).
      when('/play/:gameid', {
        templateUrl: 'Movie_Game.html',
        controller: 'LoadMovieGame'
      }).
      when('/movie', {
          templateUrl: 'Movie.html',
          controller: 'LoadMoviePageCont'
        }).
      when('/game', {
          templateUrl: 'Game.html',
          controller: 'GameController'
        }).
      when('/moviegame', {
          templateUrl: 'Movie_Game.html',
          controller: 'LoadMovieGame'
        }).
      when('/hello', {
            templateUrl: 'Hello.html'
          }).  
      when('/spotthedifference', {
          templateUrl: 'spothedifference.html',
          controller: 'LoadSpotDifferenceCont'
        }).
     otherwise({
        redirectTo: '/game'
      });
  }]);




 moviezApp.controller("LoadHomePageCont",function($scope,$routeParams,$http,$sce,$window)
 {
	 
	 $scope.movienamelist = [];
	 
	 
	 $scope.getMovieNames = function(moviename)
	 {
		 console.log("getMovieNames called" + moviename);
		
		 $http({
             method: 'GET',
             url: '/movieSuggestServlet?mvNameInitials='+ moviename ,
             headers: {'Content-Type': 'application/json'}
         }).success(function (option) 
         {
        	 $scope.movienamelist = option;
         })
	 }
	 
	 $scope.doSearch = function(movieSearch)
	 {
		 console.log("moviesearch callled : " + movieSearch);
		 var movieid = 0;
		 
		 for (i=0; i< $scope.movienamelist.length ; i++)
	     {
			 if(!(($scope.movienamelist[i][1]).localeCompare(movieSearch)))
				 {
                     movieid = $scope.movienamelist[i][0];
                     console.log("printing movie id on select " + movieid);
				 }
	     }
		 
		 $window.location.href = '#movie/'+movieid;
	 }
	 
    $http({
          method: 'GET',
          url: '/carousel',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.movies=data; 
          });  
    
    $http({
        method: 'GET',
        url: '/latestHome',
        headers: {'Content-Type': 'application/json'}
      }).success(function (data) 
        {
          $scope.latest_movies=data; 
        }); 
    
    $http({
        method: 'GET',
        url: '/upComingHome',
        headers: {'Content-Type': 'application/json'}
      }).success(function (data) 
        {
          $scope.upcoming_movies=data; 
        });
    
    $http({
        method: 'GET',
        url: '/latestTrailerServlet',
        headers: {'Content-Type': 'application/json'}
      }).success(function (data) 
        {
          $scope.latestTrailer = data;
        });
    
    
    
    $scope.buildDefaultTrailerList = function(type) {
		console.log('typs is' + type);
		if (type == 'hindiTrailers') {
			$scope.subtype = 'hindiTrailers';
			if(!($scope.latestTrailer[0].language).localeCompare("hindi"))
			{
			   $scope.list = $scope.latestTrailer[0].tailorList;
			}
		} else if (type == 'englishTrailers') {
			$scope.subtype = 'englishTrailers';
			if(!($scope.latestTrailer[1].language).localeCompare("english"))
			{
			   $scope.list = $scope.latestTrailer[1].tailorList;
			}
		} else if (type == 'telguTrailers') { 
			$scope.subtype = 'telguTrailers';
			if(!($scope.latestTrailer[2].language).localeCompare("telgu"))
			{
			   $scope.list = $scope.latestTrailer[2].tailorList;
			}
		}else if (type == 'tamilTrailers') {
			$scope.subtype = 'tamilTrailers';
			if(!($scope.latestTrailer[3].language).localeCompare("tamil"))
			{
			   $scope.list = $scope.latestTrailer[3].tailorList;
			}
		}else if (type == 'kannadaTrailers') {
			$scope.subtype = 'kannadaTrailers';
			if(!($scope.latestTrailer[4].language).localeCompare("kannada"))
			{
			   $scope.list = $scope.latestTrailer[4].tailorList;
			}
		}
	}
    
    $scope.buildDefaultSongList = function(type) {
		console.log('typs is' + type);
		if (type == 'hindiSongs') {
			$scope.subtype = 'hindiSongs';
			if(!($scope.latestTrailer[0].language).localeCompare("hindi"))
			{
			   $scope.list = $scope.latestTrailer[0].tailorList;
			}
		} else if (type == 'englishSongs') {
			$scope.subtype = 'englishSongs';
			if(!($scope.latestTrailer[1].language).localeCompare("english"))
			{
			   $scope.list = $scope.latestTrailer[1].tailorList;
			}
		} else if (type == 'telguSongs') { 
			$scope.subtype = 'telguSongs';
			if(!($scope.latestTrailer[2].language).localeCompare("telgu"))
			{
			   $scope.list = $scope.latestTrailer[2].tailorList;
			}
		}else if (type == 'tamilSongs') {
			$scope.subtype = 'tamilSongs';
			if(!($scope.latestTrailer[3].language).localeCompare("tamil"))
			{
			   $scope.list = $scope.latestTrailer[3].tailorList;
			}
		}else if (type == 'kannadaSongs') {
			$scope.subtype = 'kannadaSongs';
			if(!($scope.latestTrailer[4].language).localeCompare("kannada"))
			{
			   $scope.list = $scope.latestTrailer[4].tailorList;
			}
		}
	}
		
    $scope.getBuildTrailerDefaultList = function() {
    	console.log("getBuildTrailerDefaultList");
		return $scope.list;
	}
    
    
    $scope.getTrailerImage = function(index) {
		console.log('get Trailer Image index found is ' + index);
		return $scope.list[index].replace('embed', 'vi').replace('www', 'img')
				.replace(new RegExp('$'), '/0.jpg');
	}

	$scope.initializeModal = function(subtypeStr, index) {
		console.log('new modal id is ' + 'myModal' + index + subtypeStr);
		$scope.modalID = 'myModal' + subtypeStr + index;
		$scope.embeddedURL = $sce.trustAsResourceUrl($scope.list[index]
				+ '?autoplay=1');
	}

	$scope.clearEmbeddedURL = function() {
		$scope.embeddedURL = $sce.trustAsResourceUrl('http://www.example.com');
	}
    
    /*$scope.selFunction = function(movieInitials) {
			$http({
				method : 'GET',
				url : '/manage/managemz?movieInitals=' + movieInitials,
				headers : {
					'Content-Type' : 'application/json'
				}
			}).success(function(data) {
				$scope.movieSearchNames = data;
			});
		}*/

 });
 
 moviezApp.controller("GameController",function($scope,$routeParams,$http,gameService)
 {
    
   $scope.industryList = ["Bollywood","Hollywood","Tollywood"];
   // $scope.selectedindustry = "Select Movie Type";
   // $scope.selIndustry = function(industry)
   // {
      
     // $scope.selectedindustry = industry;
      
   // }



   $scope.hideplay = true;
   
   $scope.hideload = false;
   $scope.color = "bgcolorwhite";
   $http({
          method: 'GET',
          url: '/moviegameinit',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.idlocal = data.initiatorId;
            $scope.idremote = data.otherPlayerId;
            $scope.hideload = true;
            $scope.color = "";
            $scope.hideplay = false;
            
          }).error(function () {
                   $scope.color="";
                   $scope.hideload = true;
                   console.log("eroror response");
              });  

    $scope.startplay = function(option)
    {
      // if(!(($scope.selectedindustry).localeCompare("Select Movie Type")))
      // {
      // console.log("selected industry tyep:" + $scope.selectedindustry);
      // $scope.selectedindustry = "Bollywood";
      // }

       $scope.selectedindustry = option;
       console.log("option on play: "+ option);
       gameService.setUserChoice("computer");
       console.log("userchoice get1: "+gameService.getUserChoice());
       gameService.setIndustryName($scope.selectedindustry);
       gameService.setRemoteId($scope.idremote);
    }    
 });

 moviezApp.service('gameService', function () {
        var userchoice = "computer";
        var remoteid ="";
        var industryname ="";

        

        return {
            getUserChoice: function () {
                return userchoice;
            },
            setUserChoice: function(value) {
                userchoice = value;
            },
            getRemoteId: function () {
                return remoteid;
            },
            setRemoteId: function(value) {
                remoteid = value;
            },
            setIndustryName: function(value){
                industryname =value;
            },
            getIndustryName: function(){
                 return industryname;
            }
        };

    });

 
 moviezApp.directive('ngEnter', function () {
	    return function (scope, element, attrs) {
	        element.bind("keydown keypress", function (event) {
	            if(event.which === 13) {
	                scope.$apply(function (){
	                    scope.$eval(attrs.ngEnter);
	                });
	 
	                event.preventDefault();
	            }
	        });
	    };
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
           + '  <li ng-repeat="star in stars" ng-class="star" >'
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