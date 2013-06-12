
    <?php
		if(isset($_GET["category_number"]) && isset($_GET["song_number"])){
			

			$DATABASE_NAME = "chendiha_songstory";
			$TABLE_NAME = "table_song_story";
			$DATABASE_USER_NAME = "chendiha_tobe";
			$DATABASE_USER_PASSWORD = "561801src";
	
	
			$categoryNumber = $_GET["category_number"];
			$songNumber = $_GET["song_number"];
	
	
			$db = new mysqli("127.0.0.1", $DATABASE_USER_NAME, $DATABASE_USER_PASSWORD, $DATABASE_NAME);
	
			if(mysqli_connect_errno()){
				echo "Error: could not connect to database.";
				exit;
			}
	
	
			$queryString = "select * from `table_song_story` where `category_number` = $categoryNumber and `song_number` = $songNumber";
	
			$result = $db->query($queryString);
		
		//echo mysqli_num_rows($result);
		//if(mysqli_num_rows($result)==0){
		//	echo "no result";
		//}
	
	
			$row = $result->fetch_assoc();
	
			//echo $row["song_name"];
			//echo $row["singer_name"];
			//echo $row["category_number"];
			//echo $row["story_number"];
			//echo $row["ss_url"];
	
			if(mysqli_num_rows($result)==0){
				$response = array(
				   "is_exist" => false,
					"ss_url" => ""
				);
			}else{
	
				$response = array(
				   "is_exist" => true,
					"ss_url" => $row["ss_url"]
	
				);
	
			}
	
			echo json_encode($response);
	
	
			$result->free();
			$db->close();


		}
    ?>


