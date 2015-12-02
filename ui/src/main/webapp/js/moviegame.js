moviezApp.controller("LoadMovieGame",function($scope,$routeParams,$http,gameService)
 { 
     console.log("userchoice get: "+gameService.getUserChoice());
     console.log("route param id : "+ $routeParams.gameid);
     $scope.color="bgcolorwhite";
     $scope.hideload = false;
     if(!((gameService.getUserChoice()).localeCompare("friend")))
     {
 	        $("#game-modal").modal({backdrop: false});
 	        $scope.remoteUrl = "127.0.0.1:8080/html/Start.html#/play/" + gameService.getRemoteId();
 	        console.log("remote url: " + $scope.remoteUrl);
           
     }

    var movieName ;
    var compareChar ;
	var vowelChar ;
    var replace;
	var receivedName ;
    var maxChoices = 9;
	var usedChoices;
    $scope.alphabetvec =[];
    $scope.moviedata;

     if(!((gameService.getUserChoice()).localeCompare("computer")))
     {
     	$http({
		          method: 'GET',
		          url: '/gamemanager?preinit='+ gameService.getUserChoice() + '&id=' +  $routeParams.gameid,
		          headers: {'Content-Type': 'application/json'},
		          time:3000
     		
		        }).success(function (option) 
		        {
                     console.log("option" + option);
 	    			 initialize(option);
 	    			 $scope.color="";
 	    			 $scope.hideload = true;
	            });
	 }
    

       function initialize(option)
        {
             var success= true;
		 	 
			 compareChar = [" ","A","E","I","O","U",","];
			 vowelChar = ["A","E","I","O","U"];
			 receivedName = [];
			 usedChoices = 0;
			 $scope.alphabetvec = [["A","disabled"],["B","enabled"],["C","enabled"],["D","enabled"],["E","disabled"],["F","enabled"],["G","enabled"],["H","enabled"],["I","disabled"],["J","enabled"],["K","enabled"],
			                      ["L","enabled"],["M","enabled"],["N","enabled"],["O","disabled"],["P","enabled"],["Q","enabled"],
			                      ["R","enabled"],["S","enabled"],["T","enabled"],["U","disabled"],["V","enabled"],["W","enabled"],
			                      ["X","enabled"],["Y","enabled"],["Z","enabled"],["0","enabled"],["1","enabled"],["2","enabled"]
			                      ,["3","enabled"],["4","enabled"],["5","enabled"],["6","enabled"],["7","enabled"],["8","enabled"]
			                      ,["9","enabled"]];

			
        	$scope.moviedata = option;
        	 console.log("moviedata moviename: " + $scope.moviedata.moviename);
 
            console.log("moviedata score: " + $scope.moviedata.scoreboard);
        	console.log("moviedata: " + $scope.moviedata.scoreboard.playerScoreBoard1.totalGamePlayed);
        	var data = option.moviename.split("");
        	//console.log("mname: " + data);
			//console.log(data);
			//console.log("Char in success"+compareChar);
            movieName = data.slice();
            $scope.imageno = "" ;
            

			for(var  i =0; i< data.length; i++)
			{
				replace = true;
			//	console.log("data[i]: "+ data[i]);
				for(var j =0;j<compareChar.length;j++)
				{
					//console.log("compareChar[j]: "+ compareChar[j]);
					if(!data[i].localeCompare(compareChar[j]))
					{	
              //          console.log("set replace false");

                        replace =  false;
                    }        
			    }		
			    if(replace)
			    {
			    //	console.log("set data[i]: to _ "+ data[i]);
					data[i] = "_";
                }

				//console.log(data);
			}
            $scope.words=data.join(" ");


           //console.log("Recieved Dataa"+ movieName);
		    

        }
         $scope.selectfunction = function(option)
            {
               var userMovie = $scope.words;
		       var movie = movieName.join(" ");
               var isvowel = false;
               var isalreadyused = false;
               var alphabet = $scope.alphabetvec;

               

               if((usedChoices < maxChoices) && ((userMovie.localeCompare(movie))))
               {

               	   for(var  k =0; k< alphabet.length; k++)
                   {
		                 if(!alphabet[k][0].localeCompare(option))
		                 {
		                 	$scope.alphabetvec[k][1] = "disabled";
		                 }

                    }
	               
	               replace = true;
	         //      console.log("movieName "+ movieName );
	               receivedName = movieName.slice();
	               success = true; 
                   validchoice = false;
                  

                   for(var  j =0; j< vowelChar.length; j++)
					{
						if(!(option.localeCompare(vowelChar[j])))
						{
							        console.log("is vowel..");
									isvowel = true;   
						}     
					}

					for(var  j =0; j< compareChar.length; j++)
					{
						if(!(option.localeCompare(compareChar[j])))
						{
				//			        console.log("is already used..");
									isalreadyused = true;  

						}     
					}

					compareChar.push(option); 
				//	console.log("Push Char " + compareChar);

				   if(!isvowel)
				   {
	                   for(var  i =0; i< movieName.length; i++)
	                   {
	                   	  if(!(option.localeCompare(movieName[i]))  )
								{
				//					console.log("is valid choice..");
									validchoice = true;    
	 		                    }        
	                   }
                   }
                    
                   if(!isalreadyused  && !validchoice)
                   {
	                   if(!validchoice && (!isvowel))
	                   {
	                   	    usedChoices =  usedChoices + 1;
	                   	    
	                   	    $scope.imageno = usedChoices ;

	              //          console.log("invalid option selected");
	                   }
                   }
                   if(validchoice)
                   {
		               for(var  i =0; i< receivedName.length; i++)
					   {
					   	    replace = true;
					//   	    console.log("receivedName[i]: "+ receivedName[i]);
							for(var j =0;j<compareChar.length;j++)
							{
					//			console.log("compareChar[j]: "+ compareChar[j]);
								if(!receivedName[i].localeCompare(compareChar[j]))
								{	
		                            console.log("set replace false");
			                        replace =  false;
			                    }        
						    }		
						    if(replace)
						    {
								receivedName[i] = "_";
					//			console.log("set receivedName[i]: to _ "+ receivedName[i])
			                    success = false;

			                }
							console.log(receivedName);
							
					   }
		               $scope.words=receivedName.join(" ");
		               $scope.won = "";
		                var userMovie = $scope.words;
		                var movie = movieName.join(" ");
		            
		            	if(!(userMovie.localeCompare(movie)))
		            		{
		            			$scope.won="Congratulations!!!! You Won...";
		            			$scope.gamescore = 9 + (maxChoices - usedChoices);

						        $("#result-modal").modal({backdrop: false});		
							}


				    }
	            }
	            
	           if(usedChoices == maxChoices)
	           {
                     userMovie = $scope.words;
		             movie = movieName.join(" ");
		            
		            if(!(userMovie.localeCompare(movie)))
		            {
		            		$scope.won="Congratulations!!!! You Won...";
                            $scope.gamescore = 9 ;								
					}
					else
					{
						$scope.won="SORRY !!!! You Lose...";
						$scope.gamescore = 0;
					}
                     
                    $("#result-modal").modal({backdrop: false});
			    }
                     
            }
             
             

     $scope.sendMovieName = function(moviename)
     {
           console.log("MovieName to send to friend: " + moviename);
     }

     $scope.sendResult = function(gamescore,gamepostaction)
     {
     	    $scope.color="bgcolorwhite";
            $scope.hideload = false;
           console.log("gamescore: "+gamescore +"gamepostaction: "+ gamepostaction);
           $http({
                  method: 'GET',
                  url: '/gamemanager?score='+ gamescore + '&gamepostaction=' +  gamepostaction + '&id=' + $routeParams.gameid,
                  headers: {'Content-Type': 'application/json'}
              }).success(function (option) 
              {
              	    if(!gamepostaction.localeCompare("continue"))
                     {
                     	initialize(option);
                     	$scope.color="";
                        $scope.hideload = true;
                     }  
                    else
                    	console.log("finish");
              });
     }

 });




moviezApp.controller('refresh_control',function($scope,$interval,$http)
{
   var c=0;
   $scope.message="This DIV is refreshed "+c+" time.";

   $interval(function()
   {
   	    $http({
          method: 'GET',
          url: '/ui/HelloServlet',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.message=data; 
          });
   },1000);
});