import java.io.*;
import java.util.ArrayList;


import static java.lang.System.*;


public class A2_Graphe {

    private File file;                             // Nom du fichier
    private int sommet = -1 ;                      // Nombre de sommet
    private int arc = -1 ;                         // Nombre d'arc
    private int [][] transition;                   // Tableau de transitions
    private String [][] matrice ;                  // Matrice de valeur
    // voir le fichier L3_A2_Sommet pour plus d'info sur cette classe
    private A2_Sommet[] tableau_sommets;      // Tableau de Sommet, utile pour ordonnancement
    private A2_Sommet[] sommets_tri_rang;     // Tableau de Sommet ranger par rangs

    // Constructeur appeler directement a la creation d'un graphe
    public A2_Graphe(File file ) throws IOException
    {
        this.file = file;                                       // Donne le nom du fichier
        initialisation () ;                                     // initialise tous les attributs
        this.tableau_sommets = new A2_Sommet[this.sommet];   // Pas de problème car sommet déjà initialisé par initialisation();
        this.sommets_tri_rang = new A2_Sommet[this.sommet];
        for(int i = 0; i < this.sommet; i++)
        {
            this.tableau_sommets[i] = new A2_Sommet(transition,i);         // Initialiser le tableau de sommets
        }
    }

    // Etape 1 Lire le fichier et afficher
    public void initialisation() throws IOException
    {

        BufferedReader read = new BufferedReader (new FileReader(this.file)); // lecture du fichier
        String ligne;                                                   // recuperation de la ligne
        String [] mot;                                                 // mots de la ligne
        int compteur = 1;                                              // compteur pour savoir la ligne actuelle

        System.out.println ( "* Lecture du graphe sur fichier" );
        Write("A2_RenduFinal.txt", "* Lecture du graphe sur fichier" );

        // boucle qui lit toutes les lignes du fichier 1 à 1
        while ( (ligne = read.readLine ())!= null ){
            mot = ligne.split (" ");                             // récupérer 1 à 1 les mots de la ligne

            if (compteur == 1 ) // ligne 1
            {
                this.sommet = Integer.parseInt(mot[0]);                // premiere ligne pour le nombre de sommet
                this.matrice = new String [this.sommet][this.sommet] ; // definition de la matrice
                System.out.println ( mot[0] + " sommets" );
                Write("A2_RenduFinal.txt", mot[0] + " sommets" );

            }
            else if (compteur == 2) // ligne 2
            {
                this.arc = Integer.parseInt(mot[0]);                   // 2eme ligne pour le nombre d'arc
                this.transition = new int [this.arc][3];               // definition du tableau de transitions
                System.out.println ( mot[0] + " arcs" );
                Write("A2_RenduFinal.txt", mot[0] + " arcs" );
            }
            else // a partir de la ligne 3
            {
                // compteur -3 car on commence à la ligne 3
                this.transition [compteur-3][0]=  Integer.parseInt(mot[0]); // assigne le sommet source
                this.transition [compteur-3][1]=  Integer.parseInt(mot[1]); // assigne le sommet cible
                this.transition [compteur-3][2]=  Integer.parseInt(mot[2]); // assigne le poids
                System.out.println ( mot[0] + " -> " + mot[1] + " = " + mot[2]);
                Write("A2_RenduFinal.txt", mot[0] + " -> " + mot[1] + " = " + mot[2]); // écriture des transition
            }
            compteur ++; // incrementation du compteur
        }

        constructionMatrice (); // construction de la matrice des valeurs
    }

    // fonction qui construit la matrice des valeurs
    public void constructionMatrice ()
    {
        // initilialisation de la matrice des valeurs
        for (int i=0; i<this.matrice.length; i++)
        {
            for (int j = 0; j < this.matrice[i].length; j++)
            {

                this.matrice[i][j] = "*" ; // initialisation de toutes les cases à *
            }
        }

        // assignement des poids selon la table des transitions
        for (int i=0; i<this.transition.length; i++)
        {
            this.matrice[this.transition[i][0]][this.transition[i][1]] = String.valueOf(this.transition[i][2]);
        }
    }

    // Etape 2 les matrices
    // affichage de la matrice des valeurs avec un bon espacement
    public void afficherMatriceValeur ()
    {
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        System.out.println("Matrice des valeurs:");
        Write("A2_RenduFinal.txt", "Matrice des valeurs:");
        System.out.println();
        Write("A2_RenduFinal.txt", "");

        int maxDigit = 0;
        int digitPoid = 0;

        maxDigit = (String.valueOf(this.matrice.length)).length();

        //détermine la valeur ayant le plus de digit pour adapter l'affichage de la matrice
        for (int i=0; i<this.matrice.length; i++) {
            for (int j = 0; j < this.matrice[i].length; j++) {
                digitPoid = String.valueOf(this.matrice[i][j]).length();
                if (digitPoid > maxDigit) {
                    maxDigit = digitPoid;
                }
            }
        }

        //affichage de l'espace avant de mettre la prmière ligne avec les sommets
        digitPoid = maxDigit + 1;
        while(digitPoid >= 0) {
            System.out.print(" ");
            fullLine = fullLine + " ";
            digitPoid--;
        }

        //affichage de la première ligne contenant les sommets
        for (int k=0; k<this.matrice.length; k++){
            digitPoid = String.valueOf(k).length();
            digitPoid = (maxDigit - digitPoid) + 1;
            System.out.print(k);
            fullLine = fullLine + k;
            while (digitPoid >= 0) {
                System.out.print(" ");
                fullLine = fullLine + " ";
                digitPoid--;
            }
        }
        System.out.println();
        Write("A2_RenduFinal.txt", fullLine);
        fullLine = "";

        //affichage du contenu de la matrice des valeurs
        for (int l=0; l<this.matrice.length; l++) {
            //affichage du sommet au debut de chaque ligne
            digitPoid = String.valueOf(l).length();
            digitPoid = (maxDigit - digitPoid) + 1;
            System.out.print(l);
            fullLine = fullLine + l;
            while (digitPoid >= 0) {
                System.out.print(" ");
                fullLine = fullLine + " ";
                digitPoid--;
            }
            for (int m=0; m<this.matrice[l].length; m++) {
                //affichage des poids de transition avec ajustement des espacements en fonction de la taille
                digitPoid = String.valueOf(this.matrice[l][m]).length();
                digitPoid = (maxDigit - digitPoid) + 1;
                System.out.print(this.matrice[l][m]);
                fullLine = fullLine + this.matrice[l][m];
                while (digitPoid >= 0){
                    System.out.print(" ");
                    fullLine = fullLine + " ";
                    digitPoid--;
                }
            }
            System.out.println();
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
        }
        System.out.println();
        Write("A2_RenduFinal.txt", "");
    }

    // Convertion et affichage de la matrice adjacence
    public void afficherMatriceAdjacence () {
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        System.out.println("\n* Représentation du graphe sous forme matricielle");
        Write("A2_RenduFinal.txt", "\n* Représentation du graphe sous forme matricielle");
        System.out.println("Matrice d'adjacence:");
        Write("A2_RenduFinal.txt", "Matrice d'adjacence:");
        System.out.println();
        Write("A2_RenduFinal.txt", "");

        int maxDigit = 0;
        int digitPoid = 0;

        maxDigit = (String.valueOf(this.matrice.length)).length();

        //espace avant d'afficher les sommets sur la première ligne
        digitPoid = maxDigit + 1;
        while(digitPoid >= 0) {
            System.out.print(" ");
            fullLine = fullLine + " ";
            digitPoid--;
        }

        //affichage de la première ligne avec les sommets
        for (int i=0; i<this.matrice.length; i++){
            digitPoid = String.valueOf(i).length();
            digitPoid = (maxDigit - digitPoid) + 1;
            System.out.print(i);
            fullLine = fullLine + i;
            while (digitPoid >= 0) {
                System.out.print(" ");
                fullLine = fullLine + " ";
                digitPoid--;
            }
        }
        System.out.println();
        Write("A2_RenduFinal.txt", fullLine);
        fullLine = "";

        //affichage du contenu de la matrice
        for (int j=0; j<this.matrice.length; j++) {
            //affichage du sommet au début de chaque ligne
            digitPoid = String.valueOf(j).length();
            digitPoid = (maxDigit - digitPoid)+1;
            System.out.print(j);
            fullLine = fullLine + j;
            while (digitPoid >= 0) {
                System.out.print(" ");
                fullLine = fullLine + " ";
                digitPoid--;
            }
            for (int k=0; k<this.matrice[j].length; k++) {
                //affichage des transitions
                digitPoid = maxDigit;
                if (this.matrice[j][k] == "*") {
                    System.out.print("0"); // si la case de la matrice possède la valeur * on sort 0
                    fullLine = fullLine + "0";
                }
                else {
                    System.out.print("1"); // si la case de la matrice possède un poids on sort 1
                    fullLine = fullLine + "1";
                }
                while (digitPoid >= 0){
                    System.out.print(" ");
                    fullLine = fullLine + " ";
                    digitPoid--;
                }
            }
            System.out.println();
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
        }
        System.out.println();
        Write("A2_RenduFinal.txt", "");

    }



    // Etape 3 detection de cicuit
    // Fonction qui retourne si le graphe possède ou non un circuit
    public boolean pas_circuit (boolean afficher)
    {
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        if(afficher)
        {
            System.out.println ("\n*Détection de circuit " );
            System.out.println ("*Méthode d’élimination des points d’entrée" );
            Write("A2_RenduFinal.txt", "\n*Détection de circuit ");
            Write("A2_RenduFinal.txt", "*Méthode d’élimination des points d’entrée ");
        }



        ArrayList<String> M = new ArrayList<String>(); // sommet a traite
        ArrayList<String> CC = new ArrayList<String>();// sommet deja traite
        boolean test = true;                           // continuer tant que c'est vrai

        ArrayList<String> source = new ArrayList<String>(); // Points d’entrée actuels


        // Initialisation de M
        for (int i = 0 ;i< this.sommet ; i++ )
        {
            // Ajoute tous les sommets
            M.add(String.valueOf(i));
        }

        // Detection de circuit 2 cas pour sortir soit M vide soit circuit detecte
        while ((M.size() > 0) && (test) )
        {
            //Copie des sommets à traité
            for(int i=0; i<M.size(); i++)
            {
                source.add(M.get(i));
            }

            // Recherche les racines
            for ( int i = 0 ; i<this.transition.length ; i++ )
            {
                // on ne prend que les transitions des sommets qui ne sont pas dans CC
                if (!CC.contains(String.valueOf(transition[i][0])))
                {
                    // on retire les sommets ne sont pas racines
                    source.remove(String.valueOf(transition[i][1]));
                }
            }
            // si il reste des elements dans la copie de M alors ils sont racines
            if (source.size()>0)
            {
                // boucle pour modifier le CC et M en fonction des racines trouvees
                for(int i=0; i<source.size(); i++)
                {
                    CC.add(source.get(i)); // on ajoute au CC
                    M.remove(source.get(i)); // on retire au M
                }

                if(afficher)
                {
                    System.out.println ("points entrées : " + source);
                    fullLine = "points entrées : " + source;
                    Write("A2_RenduFinal.txt", fullLine);
                    System.out.println("Suppression des points d’entrée");
                    fullLine = "Suppression des points d’entrée";
                    Write("A2_RenduFinal.txt", fullLine);
                    System.out.println ("points restants: " + M + "\n");
                    fullLine = "points restants: " + M + "\n";
                    Write("A2_RenduFinal.txt", fullLine);
                    fullLine = "";
                }


                // on vide la copie pour recommencer le tout
                source.clear();

            }
            // si la copie est vide
            else
            {
                if(afficher)
                {
                    System.out.println("Il y a un circuit dans le graphe !");
                    test = false ;
                    Write("A2_RenduFinal.txt", "Il y a un circuit dans le graphe !");
                }


            }

        }
        if(afficher && test)
        {
            System.out.println("Pas de circuit dans le graphe !");
            Write("A2_RenduFinal.txt", "Pas de circuit dans le graphe !");
        }


        // retourne si il y a un circuit ou non, vrai pour pas de circuit faux pour un circuit
        return (test);
    }

    // Etape 4 détermination des rangs
    // Fonction qui affiche et qui retourne un tableau de sommets et son rang
    // même principe que pour la detection de circuit
    public int[][] rang (boolean afficher)
    {
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        if(afficher)
        {
            System.out.println ("*Calcul des rangs " );
            Write("A2_RenduFinal.txt", "*Calcul des rangs ");
            System.out.println ("*Méthode d’élimination des points d’entrée" );
            Write("A2_RenduFinal.txt", "*Méthode d’élimination des points d’entrée");
        }


        ArrayList<String> M = new ArrayList<String>(); // sommet a traite
        ArrayList<String> CC = new ArrayList<String>();// sommet deja traite

        boolean test = true;                           // continuer tant que c'est vrai

        ArrayList<String> rangs = new ArrayList<String>();  // rangs des sommets

        int rang = 1;                                       // rang actuel
        ArrayList<String> source = new ArrayList<String>(); // Points d’entrée actuels


        // Tout les sommet dans M
        for (int i = 0 ;i< this.sommet ; i++ )
        {
            M.add(String.valueOf(i));
        }
        while ((M.size() > 0) && (test) )
        {
            //copie les élements restant
            for(int i=0; i<M.size(); i++)
            {
                source.add(M.get(i));
            }
            // recherche de source

            for ( int i = 0 ; i<this.transition.length ; i++ )
            {
                if (!CC.contains(String.valueOf(transition[i][0])))
                {
                    source.remove(String.valueOf(transition[i][1])); // retire tout les sommets qui ont des successeurs
                }
            }
            if (source.size()>0)
            {

                for(int i=0; i<source.size(); i++)
                {
                    CC.add(source.get(i)); // on ajoute au CC
                    M.remove(source.get(i)); // on retire au M
                    rangs.add (String.valueOf(rang)); // on ajoute la valeur du rang actuel selon les sommets ajoutes
                }
                if(afficher)
                {
                    System.out.println ("rang : " + rang);
                    fullLine = "rang : " + rang;
                    Write("A2_RenduFinal.txt", fullLine);
                    System.out.println ("points entrées : " + source);
                    fullLine = "points entrées : " + source;
                    Write("A2_RenduFinal.txt", fullLine);
                    System.out.println("Suppression des points d’entrée");
                    fullLine = "Suppression des points d’entrée";
                    Write("A2_RenduFinal.txt", fullLine);
                    System.out.println ("points restants: " + M + "\n");
                    fullLine = "points restants: " + M + "\n";
                    Write("A2_RenduFinal.txt", fullLine);
                    fullLine = "";
                }


                rang ++ ; // incrémente le rang

                source.clear();
            }
            // ce cas là ne sera jamais possible car on est sur qu'il n'y a pas de circuit
            else
            {
                test = false ;
                if(afficher)
                {
                    System.out.println("Le graphe contient au moins un circuit.");
                    Write("A2_RenduFinal.txt", "Le graphe contient au moins un circuit.");
                }

            }

        }
        //s'il n'y a pas de circuit (toujours vrai car la detection de circuit est deja fait)
        if (test)
        {
            // affichage des sommets et de leur rang respective
            if(afficher)
            {
                System.out.println ("Tableau de rangs :" );
                Write("A2_RenduFinal.txt", "Tableau de rangs :");

                System.out.print ("sommets :" );
                fullLine = "sommets :";

                // affichage adapté
                for (int j =0 ; j< CC.size(); j ++)
                {
                    if (Integer.parseInt( CC.get(j)) >= 100)
                    {

                        System.out.print(" " + CC.get(j));
                        fullLine = fullLine + " " + CC.get(j);
                    }
                    else if (Integer.parseInt( CC.get(j)) >= 10)
                    {
                        System.out.print("  " + CC.get(j));
                        fullLine = fullLine + "  " + CC.get(j);
                    }
                    else
                    {
                        System.out.print("   " + CC.get(j));
                        fullLine =  fullLine + "   " + CC.get(j);
                    }

                }
                Write("A2_RenduFinal.txt", fullLine);
                fullLine = "";
                System.out.print ("\n");
                System.out.print ("rang    :");
                fullLine = "rang    :";
                for (int j =0 ; j< CC.size(); j ++)
                {
                    if (Integer.parseInt( rangs.get(j)) >= 100)
                    {
                        System.out.print(" " + rangs.get(j));
                        fullLine =  fullLine + " " + rangs.get(j);
                    }
                    else if (Integer.parseInt( rangs.get(j)) >= 10)
                    {
                        System.out.print("  " + rangs.get(j));
                        fullLine =  fullLine + "  " + rangs.get(j);
                    }
                    else
                    {
                        System.out.print("   " + rangs.get(j));
                        fullLine =  fullLine + "   " + rangs.get(j);
                    }
                }
                Write("A2_RenduFinal.txt", fullLine);
                fullLine = "";
            }
            // initialisation du tableau a retourner
            int [][] tableau = new int [CC.size()][2];

            // remplisage du tableau
            for (int j = 0 ; j<CC.size(); j++)
            {
                tableau[j][0] =Integer.parseInt( CC.get(j));   //ajoute le sommet
                tableau[j][1] =Integer.parseInt( rangs.get(j)); // ajoute le rang
            }
            return tableau; // on envoie le tableau
        }
        // si pas de circuit on envoie null (cas pas possible)
        return null;

    }



    public boolean points_entree_sortie_uniques()
    {
        int nb_entrees = 0;
        int nb_sorties = 0;
        for(int i = 0; i<this.sommet; i++)
        {
            if(tableau_sommets[i].getTable_predecesseurs().isEmpty())   // Si pas de predecesseurs => entrée
            {
                nb_entrees ++;                                          // On compte le nombre d'entrée
            }
            if(tableau_sommets[i].getTable_successeurs().isEmpty())     // Si pas de successeurs => sortie
            {
                nb_sorties ++;                                          // Compter le nombre de sortie
            }
        }
        if((nb_entrees==1)&&(nb_sorties==1))
        {
            return true;
        }
        else if (nb_entrees==1)
        {
            System.out.println("Nombre de sorties non unique");
            Write("A2_RenduFinal.txt", "Nombre de sorties non unique");
        }
        else if (nb_sorties==1)
        {
            System.out.println("Nombre d'entrées non unique");
            Write("A2_RenduFinal.txt", "Nombre d'entrées non unique");
        }
        else
        {
            System.out.println("Pas d'entrée unique, pas de sortie unique");
            Write("A2_RenduFinal.txt", "Pas d'entrée unique, pas de sortie unique");
        }
        return false;
    }

    public boolean arc_incident_exterieur_valeur_identique(A2_Sommet sommet_test)
    {
        int poids_considere = 0;
        if(!sommet_test.getPoids_successeurs().isEmpty())
        {
            poids_considere = sommet_test.getPoids_successeurs().get(0);        // On considère qu'on compare par rapport au 1er élément
            for (int i = 1; i < sommet_test.getPoids_successeurs().size(); i++) // On commence au 2e
            {
                if(poids_considere != sommet_test.getPoids_successeurs().get(i))
                {
                    System.out.println("Valeurs de poids différentes pour un même sommet source");
                    Write("A2_RenduFinal.txt", "Valeurs de poids différentes pour un même sommet source");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean tout_arc_identique(int taille_graphe)    // Fonction définie par récurrence
    {

        if(taille_graphe==0)        // condition d'arrêt
        {
            return true;
        }
        if(!arc_incident_exterieur_valeur_identique(this.tableau_sommets[taille_graphe-1]))
        {
            return false;                                   // Si l'un est faux on retourne faux
        }
        else
        {
            return tout_arc_identique(taille_graphe-1); // Sinon on continue le parcours
        }
    }

    public A2_Sommet get_point_entree()            // En supposant que l'entrée unique ait été déjà testé
    {
        for(int i = 0; i<this.sommet; i++)
        {
            if(this.tableau_sommets[i].getTable_predecesseurs().isEmpty())
            {
                return this.tableau_sommets[i]; // On retourne l'entrée si pas de prédecesseur
            }
        }
        return null;                            // Cas impossible
    }

    public A2_Sommet get_point_sortie()            // Supposant que sortie unique déjà testé
    {
        for(int i = 0; i<this.sommet; i++)
        {
            if(this.tableau_sommets[i].getTable_successeurs().isEmpty())
            {
                return this.tableau_sommets[i]; // On retourne la sortie
            }
        }
        return null;
    }

    public boolean arc_entree_zero()            // Vérfier que tous les arcs partant du point d'entrée ont un poids de 0
    {
        for(int i = 0; i < this.get_point_entree().getPoids_successeurs().size(); i++)
        {                                       // pour chaque point d'entrée on vérifie que le poids est de 0
            if (this.get_point_entree().getPoids_successeurs().get(i)!=0)
            {
                System.out.println("Poids partant du point initial non nul");
                Write("A2_RenduFinal.txt", "Poids partant du point initial non nul");
                return false;
            }
        }
        return true;
    }

    public boolean pas_transition_negatif()
    {
        for(int i = 0; i < this.transition.length; i++)
        {
            if(this.transition[i][2]<0)             // On rappelle [0] est la source [1] la cible [2] le poids dans notre table de transition
            {
                System.out.println("Arc avec un poids négatif");
                Write("A2_RenduFinal.txt", "Arc avec un poids négatif");
                return false;
            }
        }
        return true;
    }

    // Etape 5 verification si ordonancement possible
    public boolean ordonnancement_possible()            // vérification de l'ordonnancement possible
    {
        if(!this.points_entree_sortie_uniques())
        {
            System.out.println("Pas d'ordonnancement possible");
            Write("A2_RenduFinal.txt", "Pas d'ordonnancement possible");
            return false;
        }
        System.out.println("(1) Points d'entrée et sortie uniques");
        Write("A2_RenduFinal.txt", "(1) Points d'entrée et sortie uniques");
        if(!this.pas_circuit(false))
        {
            System.out.println("Pas d'ordonnancement possible");
            Write("A2_RenduFinal.txt", "Pas d'ordonnancement possible");
            return false;
        }
        System.out.println("(2) Pas de circuit");
        Write("A2_RenduFinal.txt", "(2) Pas de circuit");
        if(!this.tout_arc_identique(this.sommet))
        {
            System.out.println("Pas d'ordonnancement possible");
            Write("A2_RenduFinal.txt", "Pas d'ordonnancement possible");
            return false;
        }
        System.out.println("(3) Tout arc incident extérieur d'un sommet a la même valeur");
        Write("A2_RenduFinal.txt", "(3) Tout arc incident extérieur d'un sommet a la même valeur");
        if(!arc_entree_zero())
        {
            System.out.println("Pas d'ordonnancement possible");
            Write("A2_RenduFinal.txt", "Pas d'ordonnancement possible");
            return false;
        }
        System.out.println("(4) Tous les arcs partant du sommet initial ont un poids de 0");
        Write("A2_RenduFinal.txt", "(4) Tous les arcs partant du sommet initial ont un poids de 0");
        if(!pas_transition_negatif())
        {
            System.out.println("Pas d'ordonnancement possible");
            Write("A2_RenduFinal.txt", "Pas d'ordonnancement possible");
            return false;
        }
        System.out.println("(5) Pas d'arc avec un poids négatif");
        Write("A2_RenduFinal.txt", "(5) Pas d'arc avec un poids négatif");


        // Message de fin de test
        System.out.println("Ordonnancement possible !");
        Write("A2_RenduFinal.txt", "Ordonnancement possible !");
        return true;
    }

    public void set_rangs_graphes()                     // initial
    {
        if(this.pas_circuit(false))
        {
            int[][] table_rang = this.rang(false);
            for(int i = 0; i <table_rang.length; i++)
            {
                for(int j = 0; j < tableau_sommets.length; j++)
                {
                    if(table_rang[i][0]==tableau_sommets[j].getNom())
                    {
                        tableau_sommets[j].setRang(table_rang[i][1]);
                        sommets_tri_rang[i] = tableau_sommets[j];
                    }
                }
            }
        }
    }

    // Etape 6 ordonancement
    public void ordonnancement(){
        System.out.println("\n\n--------------------------------------------------------------------------------------" +
                "\n                               ORDONNANCEMENT                " +
                " \n--------------------------------------------------------------------------------------");
        Write("A2_RenduFinal.txt", "\n\n--------------------------------------------------------------------------------------" +
                "\n                               ORDONNANCEMENT                " +
                " \n--------------------------------------------------------------------------------------");
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        if(!this.ordonnancement_possible())
        {
            System.out.println("Pas de date au plus tôt car ordonnancement impossible");
            Write("A2_RenduFinal.txt", "Pas de date au plus tôt car ordonnancement impossible");

            return;
        }
        A2_Sommet entree = get_point_entree();
        A2_Sommet sortie = get_point_sortie();
        this.set_rangs_graphes();       // Tri par rang et classer les sommets on a un tableau de sommets classés
        if(entree != this.sommets_tri_rang[0])
            return;                     // Si l'entree pas de rang 0 on retourne
        if(sortie.getRang() != this.sommets_tri_rang[sommet-1].getRang())
        {
            System.out.println("Sortie non rang maximal");
            Write("A2_RenduFinal.txt", "Sortie non rang maximal");

            return;                     // Si la sortie n'est pas de rang maximal on retourne
        }


        // Toutes les vérifications ont été faites, on peut commencer l'ordonnancement

        ArrayList<Integer>[][] dateAuPlusTot = this.date_au_plus_tot();    // affiche les dates au plus tot
        ArrayList<Integer>[][] dateAuPlusTard = this.date_au_plus_tard(dateAuPlusTot[dateAuPlusTot.length-1][4].get(0));          // affiche les dates au plus tard

        marge(dateAuPlusTot, dateAuPlusTard);


        System.out.println("\n\n--------------------------------------------------------------------------------------" +
                "\n                              FIN ORDONNANCEMENT                " +
                " \n--------------------------------------------------------------------------------------");
        Write("A2_RenduFinal.txt", "\n\n--------------------------------------------------------------------------------------" +
                "\n                              FIN ORDONNANCEMENT                " +
                " \n--------------------------------------------------------------------------------------");
    }

    // Date au plus tot
    public ArrayList<Integer>[][] date_au_plus_tot()
    {
        this.set_rangs_graphes();
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        ArrayList<Integer> Sommet_A_Traiter = new ArrayList<>();
        ArrayList<Integer> Sommet_Ok = new ArrayList<>();

        ArrayList<Integer>[][] table_date_au_plus_tot = new ArrayList[sommet][5];
        for (int i = 0; i < this.sommet; i++)
        {
            Sommet_A_Traiter.add(this.sommets_tri_rang[i].getNom());
            for(int j = 0; j < 5; j++)
            {
                table_date_au_plus_tot[i][j] = new ArrayList<Integer>();
            }
        }
        /* Ce tableau va contenir
        Autant de [i][] qu'il y a de sommet dans le graphe
        En [i][0] Le nom du sommet actuel
        En [i][1] La liste des noms des prédécesseurs
        En [i][2] La liste des poids associés aux arcs provenant des prédécesseurs
        En [i][3] La liste des dates au plus tôt associés aux prédécesseurs
        En [i][4] La date au plus tôt associé au sommet actuel

         */

        int maximal = 0;

        /** Initialisation du tableau de la date au plus tôt **/
        System.out.println("Initialisation de la table des dates au plus tôt :");
        Write("A2_RenduFinal.txt", "Initialisation de la table des dates au plus tôt :");
        for(int i = 0; i < this.sommet; i++)
        {
            /** Le nom du sommet **/
            table_date_au_plus_tot[i][0].add(this.sommets_tri_rang[i].getNom());


            /** La liste des prédécesseurs et leurs poids **/
            for(int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++)
            {
                table_date_au_plus_tot[i][1].add(this.sommets_tri_rang[i].getTable_predecesseurs().get(j));
                table_date_au_plus_tot[i][2].add(this.sommets_tri_rang[i].getPoids_predecesseurs().get(j));
                table_date_au_plus_tot[i][3].add(0);                    // Supposant qu'on calculera les dates au plus tôt des prédécesseurs plus tard
            }


            /** Création de date au plus tôt temporaire **/

            if(this.sommets_tri_rang[i].getTable_predecesseurs().isEmpty())
            {
                maximal = 0;
            }
            else
            {
                maximal = table_date_au_plus_tot[i][2].get(0) + table_date_au_plus_tot[i][3].get(0);
                for(int k = 1; k < this.sommets_tri_rang[i].getTable_predecesseurs().size(); k++)
                {
                    if(maximal < table_date_au_plus_tot[i][2].get(k) + table_date_au_plus_tot[i][3].get(k))
                    {
                        maximal = table_date_au_plus_tot[i][2].get(k) + table_date_au_plus_tot[i][3].get(k);
                    }
                }
            }
            table_date_au_plus_tot[i][4].add(0,maximal);
        }

      

        System.out.println("\n--------------------------------------------------------------------------------------" +
                "\n                         DATE PLUS TÔT AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");

        Write("A2_RenduFinal.txt","\n--------------------------------------------------------------------------------------" +
                "\n                         DATE PLUS TÔT AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");
        /** Dates au plus tôt final **/
        for (int i = 0; i < this.sommet; i++)
        {           // Parcours de chaque sommet
            maximal = 0;
            for(int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++)
            {
                for(int k = 0; k < i; k++)
                {
                    // k sommet déjà traité
                    if(this.sommets_tri_rang[i].getTable_predecesseurs().get(j) == this.sommets_tri_rang[k].getNom())
                    {   // Si dans la liste des précédents on a le j ieme sommet qui est égal au k ieme element de la liste
                        table_date_au_plus_tot[i][3].add(j,table_date_au_plus_tot[k][4].get(0));
                        // On ajoute à la liste des dates au plus tôt des prédécesseurs la date au plus tôt du k ieme élément
                    }
                }
            }
            for(int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++)
            {
                if(maximal < table_date_au_plus_tot[i][2].get(j) + table_date_au_plus_tot[i][3].get(j))
                {
                    maximal = table_date_au_plus_tot[i][2].get(j) + table_date_au_plus_tot[i][3].get(j);
                }
            }
            table_date_au_plus_tot[i][4].remove(0);         // On retire ce qu'on avait mis au début
            table_date_au_plus_tot[i][4].add(0,maximal);    // Au final, on insère les dates au plus tôt finales
            Sommet_Ok.add(Sommet_A_Traiter.get(0));
            Sommet_A_Traiter.remove(0);
            System.out.println("\n*******************");
            Write("A2_RenduFinal.txt", "\n*******************");
            System.out.println("Nom du sommet : " + table_date_au_plus_tot[i][0].get(0));
            Write("A2_RenduFinal.txt", "Nom du sommet : " + table_date_au_plus_tot[i][0].get(0));
            System.out.println("Rang du sommet : " + this.sommets_tri_rang[i].getRang());
            Write("A2_RenduFinal.txt", "Rang du sommet : " + this.sommets_tri_rang[i].getRang());
            System.out.print("Liste des prédecesseurs : ");
            fullLine = fullLine + "Liste des prédecesseurs : ";
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++)
            {
                System.out.print(" "+table_date_au_plus_tot[i][1].get(j)+" ");
                fullLine = fullLine + " "+table_date_au_plus_tot[i][1].get(j)+" ";
            }
            System.out.println(" ");
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.print("Poids des prédecesseurs : ");
            fullLine = fullLine + "Poids des prédecesseurs : ";
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++)
            {
                System.out.print(" "+table_date_au_plus_tot[i][2].get(j)+" ");
                fullLine = fullLine + " "+table_date_au_plus_tot[i][2].get(j)+" ";
            }
            System.out.println(" ");
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.print("Dates des prédecesseurs : ");
            fullLine = fullLine + "Dates des prédecesseurs : ";
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++)
            {
                System.out.print(" "+table_date_au_plus_tot[i][3].get(j)+" ");
                fullLine = fullLine + " "+table_date_au_plus_tot[i][3].get(j)+" ";
            }
            System.out.println(" ");
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.println("Date au plus tôt de ce sommet : "+table_date_au_plus_tot[i][4].get(0));
            fullLine = fullLine + "Date au plus tôt de ce sommet : "+table_date_au_plus_tot[i][4].get(0);
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.println("Sommets traités : " + Sommet_Ok);
            fullLine = fullLine +"Sommets traités : " + Sommet_Ok;
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.println("Sommets à traiter encore : " + Sommet_A_Traiter);
            fullLine = fullLine +"Sommets à traiter encore : " + Sommet_A_Traiter;
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
        }

        // LE CALENDRIER

        String les_rangs = "" ;     // Contient les rangs
        String les_sommets = "";    // Contient les sommets
        String longueur = "";       // Contient la longueur de chaque sommet
        String predecesseur = "";   // Contient le bon prédécesseur de chaque sommet
        String Date_tot = "";       // Contient la date au plus tot de chaque sommet

        int poids = 0 ;             // pour comparer les poids des predecesseur
        int sommet_tot=-1;          // Recuperer le bon predecesseur

        // recuperation des information pour chaque sommet
        for (int i = 0; i < this.sommet; i++) {
            // Recuperation du rangs
            if ( this.sommets_tri_rang[i].getRang() <10   )
            {
                les_rangs += this.sommets_tri_rang[i].getRang() + "   ";
            }
            else
            {
                les_rangs += this.sommets_tri_rang[i].getRang() + "  ";
            }
            // Recuperation du sommet
            if ( table_date_au_plus_tot[i][0].get(0) <10   )
            {
                les_sommets += table_date_au_plus_tot[i][0].get(0) + "   ";
            }
            else
            {
                les_sommets += table_date_au_plus_tot[i][0].get(0) + "  ";
            }

            // recuperation de la longueur
            if(!this.sommets_tri_rang[i].getPoids_successeurs().isEmpty())
            {
                if ( this.sommets_tri_rang[i].getPoids_successeurs().get(0) <10   )
                {
                    longueur += this.sommets_tri_rang[i].getPoids_successeurs().get(0)  + "   ";
                }
                else
                {
                    longueur += this.sommets_tri_rang[i].getPoids_successeurs().get(0)  + "  ";
                }
            }
            // si pas de successeur
            else
            {
                longueur +=  "--  ";
            }



            //Recuperer le bon predesesseur en fonction de leurs poids
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_predecesseurs().size(); j++) {
                // recuperation du poids le plus elever
                if (poids <= table_date_au_plus_tot[i][3].get(j)) {
                    poids = table_date_au_plus_tot[i][3].get(j);
                    sommet_tot = j;   // recuperation du sommet qui correspond a ce poids

                }
            }
            // recuperation de la date au plus tot
            // si pas de predecesseur
            if (sommet_tot == -1 ) {
                predecesseur += "--  ";
                //recuperation de la date
                if ( table_date_au_plus_tot[i][4].get(0)  <10   )
                {
                    Date_tot += table_date_au_plus_tot[i][4].get(0) + "   ";
                }
                else
                {
                    Date_tot += table_date_au_plus_tot[i][4].get(0) + "  ";
                }

            }
            // si il y a le predecesseur
            else
            {
                // recuperation de ce predecesseur
                if ( table_date_au_plus_tot[i][1].get(sommet_tot)  <10   )
                {
                    predecesseur += table_date_au_plus_tot[i][1].get(sommet_tot) + "   " ;
                }
                else
                {
                    predecesseur += table_date_au_plus_tot[i][1].get(sommet_tot) + "  ";
                }
                // recuperation de la date au plus tot
                if ( table_date_au_plus_tot[i][4].get(0)  <10   )
                {
                    Date_tot += table_date_au_plus_tot[i][4].get(0) + "   ";
                }
                else
                {
                    Date_tot += table_date_au_plus_tot[i][4].get(0) + "  ";
                }


            }
            // reinitialisation des variables de comparaisons
            poids = 0;
            sommet_tot=-1;
        }

        // Affichage du calendrier
        System.out.println("\n Tableau final : "  );
        Write("A2_RenduFinal.txt", "\nTableau final : " );

        System.out.println("rangs               : " + les_rangs);
        Write("A2_RenduFinal.txt", "rangs               : " + les_rangs);
        System.out.println("sommets             : " + les_sommets);
        Write("A2_RenduFinal.txt", "sommets             : " + les_sommets);
        System.out.println("longueur            : " + longueur);
        Write("A2_RenduFinal.txt", "longueur            : " + longueur);
        System.out.println("precedent plus tot  : " + predecesseur);
        Write("A2_RenduFinal.txt", "precedent plus tot  : " + predecesseur);
        System.out.println("dates plus tot      : " + Date_tot);
        Write("A2_RenduFinal.txt", "dates plus tot      : " + Date_tot);



        System.out.println("\n--------------------------------------------------------------------------------------" +
                "\n                        FIN DATE PLUS TOT AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");

        Write("A2_RenduFinal.txt","\n--------------------------------------------------------------------------------------" +
                "\n                        FIN DATE PLUS TOT AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");
        return table_date_au_plus_tot;


    }

    //Date au plus tard
    public ArrayList<Integer>[][] date_au_plus_tard(int datePlusTot)
    {
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final

        /** Inverser les sommets **/
        reverseArraySommet();

        ArrayList<Integer> Sommet_A_Traiter = new ArrayList<>();
        ArrayList<Integer> Sommet_Ok = new ArrayList<>();

        ArrayList<Integer>[][] table_date_au_plus_tard = new ArrayList[sommet][5];
        for (int i = 0; i < this.sommet; i++) {
            Sommet_A_Traiter.add(this.sommets_tri_rang[i].getNom());

            for(int j = 0; j < 5; j++) {
                table_date_au_plus_tard[i][j] = new ArrayList<Integer>();
            }
        }
        //System.out.println(Sommet_A_Traiter +"------------------------------------------------------------"); test
        /* Ce tableau va contenir
        Autant de [i][] qu'il y a de sommet dans le graphe
        En [i][0] Le nom du sommet actuel
        En [i][1] La liste des noms des successeurs
        En [i][2] La liste des poids associés aux arcs provenant des successeurs
        En [i][3] La liste des dates au plus tard associés aux successeurs
        En [i][4] La date au plus tard associé au sommet actuel
         */

        int minimal = datePlusTot;

        /** Initialisation du tableau de la date au plus tard **/
        for(int i = 0; i < this.sommet; i++)
        {
            /** Le nom du sommet **/
            table_date_au_plus_tard[i][0].add(this.sommets_tri_rang[i].getNom());


            /** La liste des successeurs et leurs poids **/
            for(int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++) {
                table_date_au_plus_tard[i][1].add(this.sommets_tri_rang[i].getTable_successeurs().get(j));
                table_date_au_plus_tard[i][2].add(this.sommets_tri_rang[i].getPoids_successeurs().get(j));
                table_date_au_plus_tard[i][3].add(0);

            }

            /** Création de date au plus tard temporaire **/

            if(this.sommets_tri_rang[i].getTable_successeurs().isEmpty()) {
                minimal = datePlusTot;
            }
            else {
                minimal = table_date_au_plus_tard[i][3].get(0) - table_date_au_plus_tard[i][2].get(0);
                for(int k = 1; k < this.sommets_tri_rang[i].getTable_successeurs().size(); k++)
                {
                    if(minimal > table_date_au_plus_tard[i][3].get(k) - table_date_au_plus_tard[i][2].get(k))
                    {
                        minimal = table_date_au_plus_tard[i][3].get(k) - table_date_au_plus_tard[i][2].get(k);
                    }
                }
            }
            table_date_au_plus_tard[i][4].add(0,minimal);

        }

        System.out.println("\n--------------------------------------------------------------------------------------" +
                "\n                         DATE PLUS TARD AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");

        Write("A2_RenduFinal.txt","\n--------------------------------------------------------------------------------------" +
                "\n                         DATE PLUS TARD AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");
        /** Dates au plus tard final **/
        for (int i = 0; i < this.sommet; i++)
        {           // Parcours de chaque sommet
            minimal = datePlusTot;
            for(int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
                for(int k = 0; k < i; k++)
                {
                    // k sommet déjà traité
                    if(this.sommets_tri_rang[i].getTable_successeurs().get(j) == this.sommets_tri_rang[k].getNom())
                    {   // Si dans la liste des suivants on a le j ieme sommet qui est égal au k ieme element de la liste
                        table_date_au_plus_tard[i][3].add(j,table_date_au_plus_tard[k][4].get(0));
                        // On ajoute à la liste des dates au plus tôt des suivants la date au plus tôt du k ieme élément
                    }
                }
            }
            for(int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
                if(minimal > table_date_au_plus_tard[i][3].get(j) - table_date_au_plus_tard[i][2].get(j))
                {
                    minimal = table_date_au_plus_tard[i][3].get(j) - table_date_au_plus_tard[i][2].get(j);
                }
            }
            table_date_au_plus_tard[i][4].remove(0);         // On retire ce qu'on avait mis au début
            table_date_au_plus_tard[i][4].add(0,minimal);    // Au final, on insère les dates au plus tard finales

            Sommet_Ok.add(Sommet_A_Traiter.get(0));
            Sommet_A_Traiter.remove(0);
            System.out.println("\n*******************");
            Write("A2_RenduFinal.txt", "\n*******************");
            System.out.println("Nom du sommet : " + table_date_au_plus_tard[i][0].get(0));
            Write("A2_RenduFinal.txt", "Nom du sommet : " + table_date_au_plus_tard[i][0].get(0));
            System.out.println("Rang du sommet : " + this.sommets_tri_rang[i].getRang());
            Write("A2_RenduFinal.txt", "Rang du sommet : " + this.sommets_tri_rang[i].getRang());
            System.out.print("Liste des successeurs : ");
            fullLine = fullLine + "Liste des successeurs : ";
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
                System.out.print(" "+table_date_au_plus_tard[i][1].get(j)+" ");
                fullLine = fullLine + " "+table_date_au_plus_tard[i][1].get(j)+" ";
            }
            System.out.println(" ");
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.print("Poids des successeurs : ");
            fullLine = fullLine + "Poids des successeurs : ";
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
                System.out.print(" "+table_date_au_plus_tard[i][2].get(j)+" ");
                fullLine = fullLine + " "+table_date_au_plus_tard[i][2].get(j)+" ";
            }
            System.out.println(" ");
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.print("Dates des successeurs : ");
            fullLine = fullLine + "Dates des successeurs : ";
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
                System.out.print(" "+table_date_au_plus_tard[i][3].get(j)+" ");
                fullLine = fullLine + " "+table_date_au_plus_tard[i][3].get(j)+" ";
            }
            System.out.println(" ");
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.println("Date au plus tard de ce sommet : "+ table_date_au_plus_tard[i][4].get(0));
            fullLine = fullLine + "Date au plus tard de ce sommet : "+ table_date_au_plus_tard[i][4].get(0);
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.println("Sommets traités : " + Sommet_Ok);
            fullLine = fullLine +"Sommets traités : " + Sommet_Ok;
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
            System.out.println("Sommets à traiter encore : " + Sommet_A_Traiter);
            fullLine = fullLine +"Sommets à traiter encore : " + Sommet_A_Traiter;
            Write("A2_RenduFinal.txt", fullLine);
            fullLine = "";
        }

        // Affichage du calendier
        String les_rangs = "" ;    // Contient les rangs
        String les_sommets = "";   // Contient les sommets
        String longueur = "";     // La longueur du sommet
        String successeur = "";   // Contient le bon successeur de chaque sommet
        String Date = "";        //La date au plus tard


        int poids_min = 0 ;    // determine le poids minimal des successeur
        int sommet=-1;         // recuperation du sucesseur au plus tard donner par son poids

        int compteur =1;

        // récuperation des information de chaque sommets avec le bon espacement
        for (int i = 0; i < this.sommet; i++) {

            // récuperation du rang
            if ( this.sommets_tri_rang[i].getRang() <10   )
            {
                les_rangs += this.sommets_tri_rang[i].getRang() + "   ";
            }
            else
            {
                les_rangs += this.sommets_tri_rang[i].getRang() + "  ";
            }

            // récuperation du sommet
            if ( table_date_au_plus_tard[i][0].get(0) <10   )
            {
                les_sommets += table_date_au_plus_tard[i][0].get(0) + "   ";
            }
            else
            {
                les_sommets += table_date_au_plus_tard[i][0].get(0) + "  ";
            }

            // récuperation de la longueur du sommet
            if(!this.sommets_tri_rang[i].getPoids_successeurs().isEmpty())
            {
                if ( this.sommets_tri_rang[i].getPoids_successeurs().get(0) <10   )
                {
                    longueur += this.sommets_tri_rang[i].getPoids_successeurs().get(0)  + "   ";
                }
                else
                {
                    longueur += this.sommets_tri_rang[i].getPoids_successeurs().get(0)  + "  ";
                }
            }
            else
            {
                longueur +=  "--  ";
            }



            //Recuperer successeurs en fonctions des poids
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
                // premier successeurs
                if (compteur == 1 )
                {
                    poids_min = table_date_au_plus_tard[i][3].get(j); // recuperation du poids
                    sommet = j;                                       // recuperation du sommet
                }
                // successeur suivant
                else
                {
                    // Comparaison des poids et prends le plus faible
                    if (poids_min >= table_date_au_plus_tard[i][3].get(j)) {
                        poids_min = table_date_au_plus_tard[i][3].get(j); // recuperation du poids plus faible
                        sommet = j;                                       // recuperation du sommet

                    }

                }
                compteur ++; // incrementation du compteur
            }
            // Si pas de successeur
            if (sommet == -1 )
            {
                // pas de sucesseur
                successeur += "--  ";
                // recuperation de la date au plus tard de ce sommet
                if ( table_date_au_plus_tard[i][4].get(0)  <10   )
                {
                    Date += table_date_au_plus_tard[i][4].get(0) + "   ";
                }
                else
                {
                    Date += table_date_au_plus_tard[i][4].get(0) + "  ";
                }
            }
            // Un successeur
            else
            {
                // recuperation de ce successeur
                if ( table_date_au_plus_tard[i][1].get(sommet)  <10   )
                {
                    successeur += table_date_au_plus_tard[i][1].get(sommet) + "   " ;
                }
                else
                {
                    successeur += table_date_au_plus_tard[i][1].get(sommet) + "  ";
                }
                // recuperation de la date au plus tard de ce sommet
                if ( table_date_au_plus_tard[i][4].get(0)  <10  )
                {
                    Date += table_date_au_plus_tard[i][4].get(0) + "   ";
                }
                else
                {

                    Date += table_date_au_plus_tard[i][4].get(0) + "  ";
                }

            }
            // reinitialisation des variable de comparaison
            poids_min = 0;
            sommet=-1;
            compteur = 1 ;
        }
        // Affichage du tableau

        System.out.println("\n Tableau final : "  );
        Write("A2_RenduFinal.txt", "\n Tableau final : ");

        System.out.println("rangs               : " + les_rangs);
        Write("A2_RenduFinal.txt", "rangs               : " + les_rangs);
        System.out.println("sommets             : " + les_sommets);
        Write("A2_RenduFinal.txt", "sommets             : " + les_sommets);
        System.out.println("longueur            : " + longueur);
        Write("A2_RenduFinal.txt", "longueur            : " + longueur);
        System.out.println("successeur          : " + successeur);
        Write("A2_RenduFinal.txt", "successeur          : " + successeur);
        System.out.println("dates plus tard     : " + Date);
        Write("A2_RenduFinal.txt", "dates plus tard     : " + Date);

        System.out.println("\n--------------------------------------------------------------------------------------" +
                "\n                        FIN DATE PLUS TARD AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");

        Write("A2_RenduFinal.txt","\n--------------------------------------------------------------------------------------" +
                "\n                        FIN DATE PLUS TARD AFFICHAGE FINAL                " +
                " \n--------------------------------------------------------------------------------------");

        reverseArraySommet();
        return table_date_au_plus_tard;
    }

    public void reverseArraySommet()
    {
        A2_Sommet[] sommets_tri_inverse = this.sommets_tri_rang;
        int start = 0;
        int end = (sommets_tri_inverse.length) - 1;
        while (start < end) {
            // Inverse les indices
            A2_Sommet temporaire = sommets_tri_inverse[start];
            sommets_tri_inverse[start] = sommets_tri_inverse[end];
            sommets_tri_inverse[end] = temporaire;
            start++;
            end--;
        }
    }

    public void marge (ArrayList<Integer>[][] dateAuPlusTot, ArrayList<Integer>[][] dateAuPlusTard)
    {
        String  fullLine = ""; //Utilisé pour écrire les lignes print dans le fichier final
        System.out.println("");
        Write("A2_RenduFinal.txt", "");
        System.out.println("Tableau final avec marge: ");
        Write("A2_RenduFinal.txt", "Tableau final avec marge: ");
        int[][] tabFinal = new int[this.sommet][6];
        String[] nom = {
                "Rang              : ",
                "Sommet            : ",
                "Date au plus tot  : ",
                "Date au plus tard : ",
                "Marge total       : ",
                "Marge libre       : "

        };
        int valueMarge = 0;
        int Margelibre = 0 ;
        int datemin = -1 ;
        int premier = 1 ;
        int index = -1 ; // index du sommet
        for (int i = 0; i < this.sommet; i++) {
            /** calcul de la marge libre **/
            // initialisation des variable pour comparer
            datemin = -1;
            premier = 1 ;

            // recherche de de la date au plus tot min des successeur
            for (int j = 0; j < this.sommets_tri_rang[i].getTable_successeurs().size(); j++)
            {
		// recuperation de l'index du sommet dans sommets_tri_rang car nom different de sa position
                index = Recupindex (this.sommets_tri_rang[i].getTable_successeurs().get(j));

                if (premier == 1)
                {

                    // recuperation du poids le plus tot du premier successeur
                    datemin = dateAuPlusTot[index][4].get(0);
                }
                else
                    {
                        // comparaison des date et prendre le plus tot
                        if (datemin >= dateAuPlusTot[index][4].get(0)) {
                            datemin = dateAuPlusTot[index][4].get(0);
                        }
                    }
                    premier++;
            }


            if (datemin == -1)
            {
                //Pas de sucesseur
                Margelibre = 0;
            }
            else
            {
                // Marge libre = date min des sucesseur - (longuer du sommet + date au plus tot du sommet)
                Margelibre = datemin - (this.sommets_tri_rang[i].getPoids_successeurs().get(0) + dateAuPlusTot[i][4].get(0));
            }

            // Marge total
            valueMarge = (dateAuPlusTard[(this.sommet - 1) - i][4].get(0) - dateAuPlusTot[i][4].get(0));

            // on met les valeurs qui correspondent au sommet
            tabFinal[i][0] = this.sommets_tri_rang[i].getRang();
            tabFinal[i][1] = this.sommets_tri_rang[i].getNom();
            tabFinal[i][2] = dateAuPlusTot[i][4].get(0);
            tabFinal[i][3] = dateAuPlusTard[(this.sommet - 1) - i][4].get(0);
            tabFinal[i][4] = valueMarge;
            tabFinal[i][5] = Margelibre;
        }

        for (int i = 0; i < 6; i++)
        {
            fullLine = "";
            System.out.print(nom[i]);
            fullLine += nom[i] ;
            for (int j = 0; j < this.sommet; j++) {
                System.out.print(tabFinal[j][i]);
		fullLine += tabFinal[j][i] ;
                if (tabFinal[j][i] < 10)
                {
                    System.out.print("   ");
                    fullLine += "   " ;
                }
                else {
                    System.out.print("  ");
                    fullLine += "  " ;
                }
            }
            System.out.println("");
            Write("A2_RenduFinal.txt", fullLine);
        }
    }

    // recuperation de l'index d'un sommet dans sommets_tri_rang
    public int Recupindex (int a )
    {
        int index = 0 ;
        // recuperation de l'index du sommet dans la table sommets_tri_rangs
        for (int w= 0 ; w < this.sommet; w++)
        {
            if(this.sommets_tri_rang[w].getNom() == a)
            {
                 index= w;

            }
        }
        return index;
    }

    public static void Write(String FileName, String data)  //écrit data dans le fichier fileName (le crée si inexistant)
    {
        try
        {
            FileWriter fw = new FileWriter(FileName, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(data);
            pw.close();
        }

        catch (IOException e)
        {
            out.println("Erreur lors de l'écriture du fichier");
        }
    }

    public static void Write(String FileName, int data)  //surcharge de la fonction pour écrire les int dans le fichier
    {
        try
        {
            FileWriter fw = new FileWriter(FileName, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(data);
            pw.close();
        }

        catch (IOException e)
        {
            out.println("Erreur lors de l'écriture du fichier");
        }
    }
}


