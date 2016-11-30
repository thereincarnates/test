var manageapp = angular.module('mzmanageapp', [ "ngRoute" ]);

manageapp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'Home.html',
		controller : 'LoadManagePageCont'
	}).when('/manage/:optionid', {
		templateUrl : 'Home.html',
		controller : 'LoadManagePageCont'
	}).when('/newMovie', {
		templateUrl : 'NewMovie.html',
		controller : 'NewMovieAdd'
	}).otherwise({
		redirectTo : '/home'
	});
} ]);

manageapp.controller("NewMovieAdd",
		function($scope, $routeParams, $http, $window) {
	$scope.addNewMovie = function(movie) {
		
		$http({
		    url: '/manage/newMovie',
		    method: "POST",
		    data: JSON.stringify(movie),
		    headers: {'Content-Type': 'application/json'}
		}).success(function (data, status, headers, config) {
			window.alert("Done saving movie into database");
			$scope.movie.name='';
			$scope.movie.hashTag='';
			$scope.movie.wikiUrl='';
			$scope.movie.songsAndTrailers='';
			$scope.movie.videoReviews='';
			$scope.movie.interviewsAndEvents='';
		    console.log(data); 
		}).error(function (data, status, headers, config) {
			window.alert("There was an error saving movie into data base");
		    console.error('error');
		});
	}
	
});

manageapp.controller("LoadManagePageCont",
		function($scope, $routeParams, $http) {
			var itemm = '';
			$http({
				method : 'GET',
				url : '/manage/managemz',
				headers : {
					'Content-Type' : 'application/json'
				}
			}).success(function(data) {
				$scope.options = data;
				$scope.selectedItem = "Options";
			});

			$scope.selFunction = function(item) {
				$http({
					method : 'GET',
					url : '/manage/managemz?optionid=' + item,
					headers : {
						'Content-Type' : 'application/json'
					}
				}).success(function(data) {
					$scope.dataTable = data;
					$scope.selectedItem = item;
					itemm = item;
				});
			}

			var movieids = [];
			var toggle = true;

			$scope.onClick = function(row) {
				console.log(row.MovieId);
				console.log("MovieIds Table:...");
				console.log(movieids);
				for ( var key in movieids) {
					console.log("key")
					if (!(movieids[key].localeCompare(row.MovieId))) {
						console.log(key);
						movieids.splice(key, 1);
						toggle = false;
					}
				}
				if (toggle) {
					console.log("push");
					movieids.push(row.MovieId);
				} else {
					toggle = true;
				}
			}

			$scope.sendData = function() {
				console.log(itemm);
				$http({
					method : 'POST',
					url : '/manage/managemz',
					headers : {
						'Content-Type' : 'application/json'
					},
					params : {
						'movieId' : movieids
					}
				});
			}
		});
