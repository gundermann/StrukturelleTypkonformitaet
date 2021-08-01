//package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios;
//
//import static org.hamcrest.CoreMatchers.notNullValue;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
//import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
//import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
//import de.fernuni.hagen.ma.gundermann.ejb.EJBContainer;
//import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.MedicalFireFighter;
//import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.Doctor;
//import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireFighter;
//import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl.EmergencyDoctor;
//import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl.VolunteerFireFighter;
//
///**
// *
// */
//public class FindMedicalFireFighterTest {
//
//	@Before
//	public void setup() {
//		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
//
//		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
//	}
//
//	/**
//	 * Hier werden zwei Komponenten verbunden, die das erwartete Interface
//	 * strukturell jeder f�r sich, semantisch aber nur in Kombination erfuellen.
//	 * </br>
//	 * <b>COMP: A, B</b> </br>
//	 * <b>STRUCT MATCH:</b>
//	 * <ul>
//	 * <li>A = FULL
//	 * <li>B = FULL
//	 * </ul>
//	 * </br>
//	 * <b>SEM MATCH:</b>
//	 * <ul>
//	 * <li>A + B= FULL
//	 * </ul>
//	 */
//	@Test
//	public void findCombined() {
//		Class<MedicalFireFighter> desiredInterface = MedicalFireFighter.class;
//		// EJBContainer.CONTAINER.reset();
//
//		EJBContainer.CONTAINER.registerBean(FireFighter.class, new VolunteerFireFighter());
//		EJBContainer.CONTAINER.registerBean(Doctor.class, new EmergencyDoctor());
//		// EJBContainer.CONTAINER.registerBean( FirstAidTrainedPasserby.class, new
//		// TrainedPasserby() );
//		// EJBContainer.CONTAINER.registerBean( FireFighter.class, new
//		// ProfessionalFireFighter() );
//
//		// Wenn der ParaMedic mit hineingenommen wird, kann auch die Kombination aus
//		// ParaMedic und Doctor gefunden werden.
//		// EJBContainer.CONTAINER.registerBean( ParaMedic.class, new
//		// ProfessionalFireFighter() );
//		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
//				EJBContainer.CONTAINER.getRegisteredBeanInterfaces(), EJBContainer.CONTAINER::getOptBean)
//						.useHeuristicBL_NMC().useHeuristicLMF().useHeuristicPTTF().build();
//		MedicalFireFighter desiredBean = new DesiredComponentFinder(config).getDesiredComponent(desiredInterface);
//		assertThat(desiredBean, notNullValue());
//	}
//}
