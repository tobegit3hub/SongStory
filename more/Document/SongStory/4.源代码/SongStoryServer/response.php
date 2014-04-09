<!DOCTYPE html>
<html>

<head>

    <title>SongStoryServer</title>


</head>

<body>

    <?php

        $DATABASE_NAME = "db_song_story";
        $TABLE_NAME = "table_song_story";
        $DATABASE_USER_NAME = "root";
        $DATABASE_USER_PASSWORD = "561801src";


        $categoryNumber = $_GET["category_number"];
        $storyNumber = $_GET["story_number"];


        $db = new mysqli("127.0.0.1", "root", "561801src", "db_song_story");

        if(mysqli_connect_errno()){
            echo "Error: could not connect to database.";
            exit;
        }


        $queryString = "select * from `table_song_story` where `category_number` = $categoryNumber and `story_number` = $storyNumber";

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


        
    ?>


</body>

</html>

