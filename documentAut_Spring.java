//---------------------------------------- Dans Applications.proporties (Configuration Database)--------------------------------//
### DATABASE ###
spring.datasource.url=jdbc:mysql://localhost:3306/examdata?useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
#spring.datasource.driver-class-name=com.mysql.jdbc.Driver
### JPA / HIBERNATE ###
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect

server.port=8091




//-------------------------------------------- Dans L'Entité -----------------------------------------------------------------//
//**************************** tet7at fou9 el classe (A mettre en dessous de la classe )*******************************//
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table( name="Utilisateur") 
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

//***************************** Les attributs *******************************************//

///================== ID ==========================================///
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idUser")
    private Integer idUser;
	
///=============== Les autres attributs ====================================///
	private String nomUser;

	private String prenomUser;

///=========================> de Type Date <=========================================///
	@Temporal(TemporalType.DATE)
	private Date dateFabriquation;	

///=============================> de Type ENUM <========================================///

	@Column(name = "etat")
    @Enumerated(EnumType.STRING)
    Etat etat;
	
//************************************ Les Associations *******************************************************//

//============================== OnetoOne Unidirectionnelle (Projet 1---------------1 DétailProjet) ===============================//

//-------------------------- tet7at fi akber wahda wala akther wahda feha ahamia (La plus IMPORTANTE) ------------------------------------///

//---- Entité Projet ----//
@OneToOne
private Projet_Detail projetDetail;

//============================== OnetoOne Bidirectionnelle (Projet 1---------------1 DétailProjet) ===============================//

//-------------------------- tet7at fi akber wahda wala akther wahda feha ahamia (La Plus IMPORTANTE) ------------------------------------///

//---- Entité Projet ----//
@OneToOne
private Projet_Detail projetDetail;

//------------ el entité li a9al ahamia tet7at feha hedhi (la Moins IMPORTANTE ) -------------------------------------------///

//---- Entité DetailProjet ----//
@OneToOne(mappedBy="projetDetail")
private Projet projet;

//============================== OnetoMany Unidirectionnelle (Entreprise 1---------------* Equipe) ===============================//

//---------------------- tet7at fel entité li feha ba7dheha <<1>> (1) -----------------------------//

//------------- Entité Entreprise ----------------------//
@OneToMany(cascade = CascadeType.ALL)
private Set<Equipe> Equipes;

//============================== OnetoMany Bidirectionnelle (Projet 1---------------* DétailProjet) ===============================//

//  OnetoMany === ManyToOne (dans le Bidirectionnelle)

//============================== ManytoOne Unidirectionnelle (Equipe *---------------1 Entreprise) ===============================//

//----- Entité Equipe (*)----------------------//
@ManyToOne(cascade = CascadeType.ALL)
Entreprise entreprise;

//============================== ManytoOne Bidirectionnelle (Equipe *---------------1 Entreprise) ===============================//

// =====> L’attribut mappedBy est défini pour l'annotation @OneToMany (toujours au niveau de l’entité qui a la cardinalité la plus faible) dans notre cas li feha 1

//Entité Equipe
@ManyToOne
Entreprise entreprise;

//Entité Entreprise
@OneToMany(cascade = CascadeType.ALL, mappedBy="entreprise")
private Set<Equipe> Equipes;

//============================== ManytoMany Unidirectionnelle (Equipe *---------------* Projet) ===============================//

//L’équipe a plusieurs Projets et les connait. Mais, le Projet n’a aucune information sur «les équipes» auxquelles il est associé.

//Entité Equipe 
@ManyToMany(cascade = CascadeType.ALL)
private Set<Projet> projets;

//============================== ManytoMany Bidirectionnelle (Equipe *---------------* Projet) ===============================//

//L’équipe a plusieurs Projets et les connait. Chaque Projet est associé à plusieurs Equipes, et peut accéder aux attributs de la table
//équipe.

//Entité Equipe
@ManyToMany(cascade = CascadeType.ALL)
private Set<Projet> projets;

//Entité Projet
@ManyToMany(mappedBy="projets", cascade = CascadeType.ALL)
private Set<Equipe> equipes;

// OneToMany et ManyToMany ===> FetchType.LAZY
// ManyToOne et OneToOne ===> FetchType.EAGER


//************************************ JPQL *******************************************************//

//--------------------------- SELECT -------------------------------------------//

@Query("SELECT e FROM Entreprise e WHERE e.adresse =:adresse")
List<Entreprise> retrieveEntreprisesByAdresse(@Param("adresse") String adresse);

// OU //

@Query("SELECT e FROM Entreprise e WHERE e.adresse = ?1")
List<Entreprise> retrieveEntreprisesByAdresse(String adresse);

//Cette méthode permet d’afficher les projets qui ont un coût supérieur à coût donné dt et une technologie donnée.

@Query("SELECT projet FROM Projet projet, ProjetDetail detail where "

+ "detail.idProjetDetail = projet.projetDetail.idProjetDetail "

+ "and detail.technologie =:technologie "

+ "and detail.cout_provisoire >:cout_provisoire")

List<Projet> retrieveProjetsByCoutAndTechnologie(@Param("technologie") String technologie,

@Param("cout_provisoire") Long cout_provisoire);

//------------------------------------- UPDATE ------------------------------------------------------------------------//

// Cette méthode permet de mettre à jour l’adresse de l’entreprise.

@Modifying

@Query("update Entreprise e set e.adresse = :adresse where e.idEntreprise =
:idEntreprise")

int updateEntrepriseByAdresse(@Param("adresse") String adresse,
@Param("idEntreprise")

Long idEntreprise);

//-------------------------------------- DELETE --------------------------------------------------------------//

@Modifying

@Query("update Entreprise e set e.adresse = :adresse where e.idEntreprise =:idEntreprise")

int updateEntrepriseByAdresse(@Param("adresse") String adresse,@Param("idEntreprise") Long idEntreprise);

// OU //

@Modifying

@Query("DELETE FROM Entreprise e WHERE e.adresse= ?1")

int deleteFournisseurByCategorieFournisseur(String adresse);

//------------------------ PAS de INSERT -------------------------------------------------------------------//


//************************************ Spring MVC-REST *******************************************************//



#Server configuration

server.port=8089

server.servlet.context-path=/SpringMVC



// dans le package Controller //

// !! A mettre en dessous de la classe (fou9 el classe) !! //

@RestController
@RequestMapping("/vm") //thot esm el classe

//------------------ fi west el classe thothom ---------------------------------------------//

  @Autowired
  ProjetService projetService;
	
//--------------------------------- Affichage de tout les Projets --------------------------------------//

 @Operation(description = "Affichage de la liste des projets")
 @GetMapping("/projet")
 private List<Projet> allProjets(){
    return projetService.retrieveAllProjets();
    }
	
	
//--------------------------------- Ajout d'un Projet --------------------------------------//

 @Operation(description = "Ajout d'un projet")
    @PostMapping("/projet")
    @ResponseBody
    public Projet addProjet(@RequestBody Projet p) {
        return projetService.addProjet(p);
    }

//--------------------------------- Modification d'un Projet --------------------------------------//

    @Operation(description = "Modification d'un projet ")
    @PutMapping("/update")
    @ResponseBody
    public Projet updateProjet(@RequestBody  Projet p) {
        return projetService.updateProjet(p);
    }
//--------------------------------- Suppression d'un Projet --------------------------------------//

    @Operation(description = "Suppression d'un projet")
    @DeleteMapping ("/projet/{idProjet}")
    @ResponseBody
    public void removeProjet(@PathVariable("idProjet") Long idProjet) {
        projetService.removeProjet(idProjet);
    }

//--------------------------------- Affichage d'un Projet par ID--------------------------------------//

    @Operation(description = "Affichage d'un projet par Id")
    @GetMapping("/projet/{idProjet}")
    private Projet displayById(@PathVariable("idProjet") long idProjet){
        return projetService.retrieveProjet(idProjet);
    }
//--------------------------------- Demarrer Instance User (USER-VMWare)--------------------------------------//

@PutMapping("/demarrer/{idvm}")
    @ResponseBody
    public void demarrerInstanceUser(@PathVariable("idvm") int idvm){
        vmService.demarrerInstanceUser(idvm);
    }
//--------------------------------- Arreter Instance User (USER-VMWare)--------------------------------------//

    @PutMapping("/arreter/{idvm}")
    @ResponseBody
    public void arreterInstanceUser(@PathVariable("idvm") int idvm){
        vmService.arreterInstanceUser(idvm);
    }
	
	//--------------------------------- Affecter VM lel User--------------------------------------//


    @PutMapping("/affecterVmU/{id-vm}/{id-user}")
    public void affecterVmuser(@PathVariable("id-vm") int idVm, @PathVariable("id-user") int idUser){
        vmService.affecterVmuser(idVm,idUser);
    }
	
	//--------------------------------- Affecter VM lel Datacentre--------------------------------------//

    @PutMapping("/affecterV/{id-vm}")
    public void affecterVm(@PathVariable("id-vm") int idVm){
        vmService.affecterVm(idVm);
    }
	
	//--------------------------------- Ajouter ET Affecter Formation lel Formateur--------------------------------------//

	
	@PostMapping("/ajoutFortoFr/{idFormateur}")
    public void ajouterEtAffecterFormationAFormateur(@RequestBody Formation formation, @PathVariable("idFormateur") Integer idFormateur){
        formationService.ajouterEtAffecterFormationAFormateur(formation,idFormateur);
    }
	
	//--------------------------------- Affecter Appreunant lel Formation--------------------------------------//
	
	
	 @PutMapping("/affecter")
    @ResponseBody
    public void affecterApprenantFormation(@RequestParam("idApprenant") Integer idApprenant, @RequestParam("idFormation") Integer idFormation){
        appreunantService.affecterApprenantFormation(idApprenant,idFormation);
    }


// dans le package Service (la Classe !!!) //
// fou9 el classe thot @Service wou matensech l'implementation de l'interface du service

/*
@Service
public class FormationService implements IFormationService

*/

// fel affecter lezmek tzid el repo de la 2ème classe

// Dans FormationService.java //

// @Autowired
  //FormateurRepository formateurRepository;
  
 
//------------------ Affecter Formation lel Formateur -------------------------------//

 @Override
    public void ajouterEtAffecterFormationAFormateur(Formation formation, Integer idFormateur) {
       formation.setFormateur(formateurRepository.findById(idFormateur).get());
        formationRepository.save(formation);
    }
}

//------------------ Affecter Appreunant lel Formation  -------------------------------//

 @Override
    public void affecterApprenantFormation(Integer idApprenant, Integer idFormation) {
       Formation f= formationRepository.findById(idFormation).get();


       if (f.getFormationApprenant()== null || f.getFormationApprenant().size()==0){
           f.getFormationApprenant().add(apprenantRepository.findById(idApprenant).get());
           formationRepository.save(f);
       }
       else if (f.getFormationApprenant().size() < f.getNbrMaxParticipant()) {
           //kif tebda el liste fer8a
           Set<Apprenant> la= new HashSet<Apprenant>();
           la.add(apprenantRepository.findById(idApprenant).get());
           f.setFormationApprenant(la);
           formationRepository.save(f);
       }

    }
	
//------------------------- Ajout bel Void ---------------------------------------//

@Override
    public void ajouterApprenant(Apprenant apprenant) {
        Apprenant appadd= apprenantRepository.save(apprenant);
    }
	
//----------------------------- Bech testi bel SWAGGER.ui -------------------------------------//

// http://server:port/context-path/swagger-ui.html ==> kif ma3adekech context-path twalli ta3mel http://server:port/swagger-ui.html

//exp: http://localhost:8080/swagger-ui/index.html











