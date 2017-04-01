moviezApp
		.controller(
				"LoadMoviePageCont",
				function($scope, $routeParams, $http, $sce, $window) {

					$scope.modalID = 'myModal0';
					$scope.posIndex = 0;
					$scope.negIndex = 0;
					$scope.commentWindowSize = 5;

					console.log("printing the roureparams:"
							+ $routeParams.movieid);
					if ($routeParams.movieid == undefined) {
						$routeParams.movieid = '';
					}

					$http({
						method : 'GET',
						url : '/movieplot?movieId=' + $routeParams.movieid,
						headers : {
							'Content-Type' : 'application/json'
						}
					}).success(function(data) {
						console.log('successfull');
						$scope.movieobj = data;
						console.log("ZZZZZZZZZZZDDDDDDDDDD" + $scope.movieobj);
					});
					
					$http({
						method : 'GET',
						url : '/movieReview?movieId=' + $routeParams.movieid,
						headers : {
							'Content-Type' : 'application/json'
						}
					}).success(function(data) {
						console.log('successfull');
						$scope.moviereviews = data;
						$scope.commentType = 'overall';
						console.log("ZZZZZZZZZZZDDDDDDDDDD" + $scope.moviereviews);
						$scope.fillReviewType('overall');
					});
					
					
					

					$scope.movienamelist = [];

					$scope.getMovieNames = function(moviename) {
						console.log("getMovieNames called" + moviename);

						$http(
								{
									method : 'GET',
									url : '/movieSuggestServlet?mvNameInitials='
											+ moviename,
									headers : {
										'Content-Type' : 'application/json'
									}
								}).success(function(option) {
							$scope.movienamelist = option;
						})
					}

					$scope.doSearch = function(movieSearch) {
						console.log("moviesearch callled : " + movieSearch);
						$window.location.href = '#movie/' + movieSearch;
					}
					
					$scope.reviewNext = function(){
						if($scope.reviews.length > $scope.reviewIndex + $scope.commentWindowSize){
							$scope.reviewIndex = $scope.reviewIndex  + $scope.commentWindowSize;
						}
					}
					
					$scope.reviewPrev =  function(){
						if($scope.reviewIndex - $scope.commentWindowSize >=0){
							$scope.reviewIndex = $scope.reviewIndex -  $scope.commentWindowSize;
						}
					}

					$scope.buldDefaultList = function(type) {
						console.log('type is' + type);
						if (type == 'songAndTrailers') {
							$scope.subtype = 'songAndTrailers';
							$scope.list = $scope.movieobj.songAndTrailers;
						} else if (type == 'videoReviews') {
							$scope.subtype = 'videoReviews';
							$scope.list = $scope.movieobj.videoReviews;
						} else if (type == 'interviewAndEvents') {
							$scope.subtype = 'interviewAndEvents';
							$scope.list = $scope.movieobj.interviewAndEvents;
						}
					}

					$scope.getBuildDefaultList = function() {
						console.log("getBuildDefaultList being called"
								+ $scope.list);
						return $scope.list;
					}

					$scope.getSongAndTrailerImage = function(index) {
						console.log("I am being called");
						var imageUrl = 'https://img.youtube.com/vi/'
								+ $scope.list[index].match('[^=]+$')[0]
								+ '/0.jpg';
						console.log("image URL " + imageUrl);
						return imageUrl;
					}

					$scope.initializeModal = function(subtypeStr, index) {
						console.log('new modal id is ' + 'myModal' + index
								+ subtypeStr);
						$scope.modalID = 'myModal' + subtypeStr + index;
						$scope.embeddedURL = $sce
								.trustAsResourceUrl('https://www.youtube.com/embed/'
										+ $scope.list[index].match('[^=]+$')[0]
										+ '?autoplay=1');
					}

					$scope.clearEmbeddedURL = function() {
						$scope.embeddedURL = $sce
								.trustAsResourceUrl('http://www.example.com');
					}

					$scope.fillReviewType = function(reviewtype) {
						$scope.posIndex = 0;
						$scope.negIndex = 0;
						$scope.reviewIndex = 0;
						console.log("fillReviewType being called");
						if (reviewtype == 'overall') {
							$scope.commentType = 'overall';
							$scope.reviews = $scope.moviereviews.overall;
						} else if (reviewtype == 'story') {
							console.log("reviews for story being called");
							$scope.commentType = 'story';
							$scope.reviews = $scope.moviereviews.story;
						} else if (reviewtype == 'acting') {
							$scope.commentType = 'acting';
							$scope.reviews = $scope.moviereviews.acting;
						} else if (reviewtype == 'direction') {
							$scope.commentType = 'direction';
							$scope.reviews = $scope.moviereviews.direction;
						} else if (reviewtype == 'music') {
							$scope.commentType = 'music';
							$scope.reviews = $scope.moviereviews.music;
						}
					}

					$scope.isClicked = true;
					$scope.isNotClicked = false;
					$scope.showComment =true;
					

					$scope.commentClick = function() {
						console.log("Commit Column Clicked");
						$scope.isClicked = false;
						$scope.isNotClicked = true;
					
					}
					$scope.submitComment = function() {
						$scope.isClicked = true;
						$scope.isNotClicked = true;
						$scope.showComment =false;
						
						
						var data={
								movieId:$scope.movieobj.movieId,
								userId: "0",
								comment:$scope.UserComment,
								overallRating :0, //$scope.overallRating,
								storyRating: 0, //$scope.storyRating,
								actingRating:0, //$scope.actingRating,
								directionRating:0, //$scope.directionRating,
								musicRating:0 //$scope.musicRating
								
						};
						
						$http.post('/movieUserComment',JSON.stringify(data))
						.success(function(option)
						{
							console.log("testingpost " + option.movieId);
						});

						
					}

				});
