<?php

$connect = mysqli_connect('localhost', 'root', 'mattdu02210boss2', 'test');


if($_GET['mode'] == 'login'){
    $type = ucfirst($_GET['type']);
    $q = 'SELECT login FROM '.$type.' WHERE login = "'.$_GET['login'].'" AND mdp = "'.$_GET['mdp'].'";';
    $r = mysqli_query($connect, $q);
    $fetch_r = mysqli_fetch_assoc($r);
    if(!is_null($fetch_r) && $fetch_r['login'] == $_GET['login']){
        echo "match";
    }else{
        echo "nomatch";
    }
}



elseif($_GET["mode"] == 'saisie'){
    $user = $_GET["user"];
    $q = 'SELECT * FROM LigneFraisForfait WHERE mois = "'.date("Y").date("m").'" AND idVisiteur = (SELECT id FROM Visiteur WHERE login = "'.$user.'")';
    $r = mysqli_query($connect, $q);
    while($temp = mysqli_fetch_assoc($r)){
        $key = $temp['idFraisForfait'];
        $fetch_r[$key] = $temp;
    }
    $jsonarray["forfait"] = $fetch_r;
    //
    unset($fetch_r);
    //
    
    if($jsonarray["forfait"]  != NULL){
        echo json_encode($jsonarray);
    }else{
        echo "none";
    }
}




elseif($_GET['mode'] == 'envoi'){

    $usercode = mysqli_fetch_assoc(mysqli_query($connect, "SELECT id FROM Visiteur WHERE login = '".$_GET['user']."';"));

    $usercode = $usercode['id'];

    $tab = Array(
        "ETP" => $_GET['etp'],
        "KM" => $_GET['km'],
        "NUI" => $_GET['nui'],
        "REP" => $_GET['rep']);

    $total = $_GET['etp'] + $_GET['km'] + $_GET['nui'] + $_GET['rep'];

    if($_GET['exist'] == 1){
        foreach($tab as $key => $value){
            $q = "UPDATE LigneFraisForfait SET quantite = $value WHERE idFraisForfait = '$key' AND mois = '".date("Y").date("m")."' AND idVisiteur = '$usercode';";
            if(mysqli_query($connect, $q)){
                $error = 0; 
            }else{
                $error += 1;            
            }
            //echo $q;
        }
        $q = "UPDATE FicheFrais SET montantValide = '$total', dateModif = NOW() WHERE mois = '".date("Y").date("m")."' AND idVisiteur = '$usercode';";
        mysqli_query($connect, $q);
        

        if($error == 0) echo 'Modified'; else echo 'dberror';
        
    }elseif($_GET['exist'] == 0){
        foreach($tab as $key => $value){
            $q = "INSERT INTO LigneFraisForfait(idVisiteur, mois, idFraisForfait, quantite) VALUES('$usercode', '".date("Y").date("m")."', '$key', '$value');";            
            //echo $q;
            if(mysqli_query($connect, $q)){
                $error = 0; 
            }else{
                $error += 1;            
            }
        }
        $q = "INSERT INTO FicheFrais(idVisiteur, mois, montantValide, idEtat) VALUES('$usercode', '".date("Y").date("m")."', '$total', 'CR');";
        mysqli_query($connect, $q);
        
        if($error == 0) echo 'Inserted'; else echo 'dberror';
    }
}



elseif($_GET['mode'] == 'envoihorsforfait'){
    $usercode = mysqli_fetch_assoc(mysqli_query($connect, "SELECT id FROM Visiteur WHERE login = '".$_GET['user']."';"));

    $usercode = $usercode['id'];
    $libelle = $_GET['libelle'];
    $montant = $_GET['montant'];
    $date = $_GET['date'];
    if($libelle != "" || $montant != "" || $date != ""){
        $q = "INSERT INTO LigneFraisHorsForfait(idVisiteur, mois, libelle, date, montant) VALUES('$usercode', '".date("Y").date("m")."', '$libelle', '$date', '$montant');";
        echo $q;
        if(mysqli_query($connect, $q)){
            $error = 0; 
        }else{
            $error += 1;            
        }
    }else{
        echo "emptyline<br>";
    }
    

    if($error == 0) echo 'Inserted'; else echo 'error';
}



elseif($_GET['mode'] == 'consult'){

    $usercode = mysqli_fetch_assoc(mysqli_query($connect, "SELECT id FROM Visiteur WHERE login = '".$_GET['user']."';"));

    $usercode = $usercode['id'];
    
    $q = "SELECT * FROM LigneFraisForfait WHERE mois LIKE '%".date("Y").date("m")."%' AND idVisiteur = '$usercode';";
    $r = mysqli_query($connect, $q);
    while($temp = mysqli_fetch_assoc($r)){
        $key = $temp['idFraisForfait'];
        $fetch_r[$key] = $temp;
    }
    $jsonarray["forfait"] = $fetch_r;

    unset($fetch_r);
    unset($temp);

    $q2 = "SELECT * FROM LigneFraisHorsForfait WHERE mois LIKE '%".date("Y").date("m")."%' AND idVisiteur = '$usercode';";
    $r2 = mysqli_query($connect, $q2);
    while($temp2 = mysqli_fetch_assoc($r2)){
        $fetch_r2[$key] = $temp2;
    }
    $jsonarray["horsforfait"] = $fetch_r2;

    $q3 = mysqli_fetch_assoc(mysqli_query($connect, "SELECT idEtat FROM FicheFrais WHERE mois LIKE '%".date("Y").date("m")."%' AND idVisiteur = '$usercode';"));
    //var_dump($q3['idEtat']);

    $q4 = mysqli_fetch_all(mysqli_query($connect, "SELECT montant FROM LigneFraisHorsForfait WHERE mois LIKE '%".date("Y").date("m")."%' AND idVisiteur = '$usercode';"));

    foreach($q4 as $line){
        $total += $line['0'];
    }

    $jsonarray['etat'] = $q3['idEtat'];
    $jsonarray['horsforfaittotal'] = $total;

    if($jsonarray["forfait"]  != NULL){
        echo json_encode($jsonarray);
    }else{
        echo "none";
    }
    //var_dump($jsonarray['horsforfait']);
    //echo $q.'<br>'.$q2;
}


mysqli_close($connect);
