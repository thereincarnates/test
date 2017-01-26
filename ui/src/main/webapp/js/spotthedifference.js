 moviezApp.controller("LoadSpotDifferenceCont",function($scope,$routeParams,$http,$window)
     {
     	 var imagesize;
	     var defectcoordsactual =[];
	     var defectcoords = [];
	     var numOfDefectsGuessed = 0;
	     var img;
      	 var cnvs;
      	 var width;
      	 var height ;
      	 var offsetleft;
      	 var offsettop ;

	      $http({
                  method: 'GET',
                  url: '/spotdifference' ,
                  headers: {'Content-Type': 'application/json'}
                }).success(function (option) 
                {
    	           $scope.imageProperty = option;
    	           console.log("image property: " + $scope.imageProperty.imageWidth);
    	           
    	           imagesize = {width:$scope.imageProperty.imageWidth,height:$scope.imageProperty.imageHeight};
    	  	        	      	 for (var i =0; i<($scope.imageProperty.diffCoordinates).length;i++)
    	           {
    	  	         defectcoordsactual[i] = {XCoord:$scope.imageProperty.diffCoordinates[i].xCoord , YCoord:$scope.imageProperty.diffCoordinates[i].yCoord , 
    	  	        		                  Radius:$scope.imageProperty.diffCoordinates[i].radius, Guessed : "false"};
    	      	                    
    	      	     defectcoords[i] = {XCoord:$scope.imageProperty.diffCoordinates[i].xCoord , YCoord:$scope.imageProperty.diffCoordinates[i].yCoord , 
    	  	                                  Radius:$scope.imageProperty.diffCoordinates[i].radius, Guessed : "false"};
    	      	
    	           }
    	      		
    	      	     	      	 
    	      	  img = document.getElementById('modifiedimage');
    	      	  cnvs = document.getElementById("myCanvas");

    	      	 cnvs.style.position = "absolute";
    	      	 cnvs.style.left = img.offsetLeft + "px";
    	      	 cnvs.style.top = img.offsetTop + "px";
    	      	 cnvs.width = img.clientWidth;
    	      	 cnvs.height = img.clientHeight;

    	      	 
    	      	 
    	      	 var width = img.clientWidth;
    	      	 var height = img.clientHeight;
    	      	 var offsetleft = $("#modifiedimage").offset().left;
    	      	 var offsettop = $("#modifiedimage").offset().top;

                })
    	
    	 
             	 
    	
    
    	 $scope.showCoords = function(event)
         {
    		 var w = $(window);
   		     width = img.clientWidth ;
       	     height = img.clientHeight ;
       	  
       	     cnvs.style.position = "absolute";
     	     cnvs.style.left = img.offsetLeft + "px";
     	     cnvs.style.top = img.offsetTop + "px";
     	     cnvs.width = img.clientWidth;
     	     cnvs.height = img.clientHeight;

     	 
       	    offsetleft = ($("#modifiedimage").offset().left)-w.scrollLeft();
       	    offsettop = $("#modifiedimage").offset().top-w.scrollTop();
       	 
       	 
    		  for (var defect=0; defect<defectcoords.length;defect++)
    		  {
    			 defectcoords[defect].XCoord =  defectcoordsactual[defect].XCoord  * (width/imagesize.width);
    			 defectcoords[defect].YCoord =  defectcoordsactual[defect].YCoord * (height/imagesize.height); 
    		  }
    		 
    		  for (var defect=0; defect<defectcoords.length;defect++)
    		  {
    			 console.log("defect XCoordinate: " +defectcoords[defect].XCoord +" defect YCoordinate: " +defectcoords[defect].YCoord) 
    		  }
    		 
        	 
        	 console.log("Image Width: " + width + " height: " + height + " Left Coordinate: " + offsetleft
        			        + " Top Coordinate: " +offsettop);
        	 
    		 var clickedX = event.clientX - offsetleft ;
    		 var clickedY = event.clientY - offsettop; 
    		 
    		 console.log("Clicked x: " + clickedX + " y: " + clickedY);
    		 
    		 
    		 
    		 for (var defect=0; defect<defectcoords.length;defect++)
    		 {
    			 console.log("defect guessed or not: "+ defectcoords[defect].Guessed);
    			 if(!((defectcoords[defect].Guessed).localeCompare("false")))
    		     {
    			     var distance = Math.sqrt((Math.pow(clickedX-defectcoords[defect].XCoord, 2)) 
    					                   + (Math.pow(clickedY-defectcoords[defect].YCoord,2)));
    		         if(distance < defectcoords[defect].Radius)
    		         {
    		        	 defectcoords[defect].Guessed = "true";
    		        	 
    		    	     console.log("Defect Lies inside the circle , correct guess : distance: " + 
    		    			                  distance + " Radius: " + defectcoords[defect].Radius + " Guessed: " +defectcoords[defect].Guessed);
    		    	      
    		    	     
    		    	    
    		    	     
    		    	     numOfDefectsGuessed++;
    		    	     
    		    	     if(numOfDefectsGuessed == defectcoords.length)
    	    		     {
    	    			     console.log("You Won !!!!!!!!!!!");	 
    	    			 }
    		    	     
    		    	     break;
    		         }
    		         else
    		         {
    		    	     console.log("Defect Lies outside/on the circle , incorrect guess : distance: " + distance + " Radius: " 
    		    			            + defectcoords[defect].Radius);
    		         }
    		     }
    			 
    		 }
    		 
    		 for (var defect=0; defect<defectcoords.length;defect++)
    		 {
    			 var ctx=cnvs.getContext("2d");
    			 if(!((defectcoords[defect].Guessed).localeCompare("true")))
    		     {
	    	        ctx.beginPath();
	    	        
	    	        ctx.arc(defectcoords[defect].XCoord,defectcoords[defect].YCoord,defectcoords[defect].Radius,0,2*Math.PI);
	    	        ctx.lineWidth = 3;

	    	       
	    	        ctx.strokeStyle = 'red';
	    	        ctx.stroke();
    		     }
    		 }
    		 
    			 
         }
    	 
    	 
    	 /*code called on window resize*/
    	 var w1 = angular.element($window);
    	 w1.bind('resize', function () {
    		 
    		 var w = $(window);
   		     width = img.clientWidth ;
       	     height = img.clientHeight ;
       	  
       	     cnvs.style.position = "absolute";
     	     cnvs.style.left = img.offsetLeft + "px";
     	     cnvs.style.top = img.offsetTop + "px";
     	     cnvs.width = img.clientWidth;
     	     cnvs.height = img.clientHeight;

     	 
       	    offsetleft = ($("#modifiedimage").offset().left)-w.scrollLeft();
       	    offsettop = $("#modifiedimage").offset().top-w.scrollTop();
       	 
       	 
    		  for (var defect=0; defect<defectcoords.length;defect++)
    		  {
    			 defectcoords[defect].XCoord =  defectcoordsactual[defect].XCoord  * (width/imagesize.width);
    			 defectcoords[defect].YCoord =  defectcoordsactual[defect].YCoord * (height/imagesize.height); 
    		  }
    		  
    		  for (var defect=0; defect<defectcoords.length;defect++)
     		 {
     			 var ctx=cnvs.getContext("2d");
     			 if(!((defectcoords[defect].Guessed).localeCompare("true")))
     		     {
 	    	        ctx.beginPath();
 	    	        
 	    	        ctx.arc(defectcoords[defect].XCoord,defectcoords[defect].YCoord,defectcoords[defect].Radius,0,2*Math.PI);
 	    	        ctx.lineWidth = 3;

 	    	       
 	    	        ctx.strokeStyle = 'red';
 	    	        ctx.stroke();
     		     }
     		 }
    		 
    		
    		 console.log('resize......');
    		 });
     });
     
     
     
