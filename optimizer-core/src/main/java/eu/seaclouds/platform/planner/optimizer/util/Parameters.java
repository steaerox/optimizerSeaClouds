package eu.seaclouds.platform.planner.optimizer.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.SuitableOptions;
import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.TopologyElement;

public class Parameters {

    private static Map<String, Object> appMap;
    private static SuitableOptions appInfoSuitableOptions;
    private static Map<String, Object> allCloudOffersMAP;
    private static String appModel;
    private static String suitableCloudOffers;
    private static Topology topology;

    static Logger log = LoggerFactory.getLogger(Parameters.class);

    // public static void initializeProblem(String appModel, String
    // suitableCloudOffer) {
    // appModel_ = appModel;
    // suitableCloudOffers_ = suitableCloudOffer;
    //
    // //Get app characteristics
    // getAppCharacteristics(appModel);
    //
    // // Get cloud offers
    // getMapOfSuitableCloudOffers(suitableCloudOffer);
    //
    // // TODO: Obtain Application topology. At 10/02/2015 this information is
    // // not included in the YAML. It's not possible to retrieve it
    // getApplicationTopology();
    //
    // // TODO: Remove the following temporal management of the lack of
    // // topology. Create an incorrect and ad-hoc one to keep the system
    // // working
    // if (topology_ == null) {
    // //
    // log.error("Topology could not be parsed. Not known quantity of calls between modules. Assuming the dummy case where"
    // // +
    // "all modules are called in sequence. The order of calls is random}");
    // createAdHocTopologyFromSuitableOptions(appInfoSuitableOptions_);
    // }
    // }

    public static void initializeProblem(String appModel,
            String suitableCloudOffer) {
        appModel = appModel;
        suitableCloudOffers = suitableCloudOffer;

        // Get app characteristics
        appMap = YAMLoptimizerParserNew.GetMAPofAPP(appModel);
        log.debug("appMap_ read");

        // Get cloud offers
        allCloudOffersMAP = YAMLoptimizerParserNew
                .getMAPofCloudOffers(suitableCloudOffer);
        log.debug("allCloudOfferMAP READ");

        topology = YAMLoptimizerParserNew.getApplicationTopology(appMap,
                allCloudOffersMAP);
        log.debug("topology read");
    }

    private static void getAppCharacteristics(String appModel) {
        appMap = YAMLoptimizerParserNew.GetMAPofAPP(appModel);
    }

    private static void getMapOfSuitableCloudOffers(String suitableCloudOffer) {
        allCloudOffersMAP = YAMLoptimizerParserNew
                .getMAPofCloudOffers(suitableCloudOffer);
    }

    private static void getApplicationTopology() {
        topology = YAMLoptimizerParserNew.getApplicationTopology(appMap,
                allCloudOffersMAP);
    }

    // TODO: Remove this method to avoid finishing weird executions when the
    // YAML does not contain all the information.
    // Later it will be better an exception than a weird result.
    private static Topology createAdHocTopologyFromSuitableOptions(
            SuitableOptions appInfoSuitableOptions) {
        Topology topology = new Topology();

        TopologyElement current = null;
        TopologyElement previous = null;

        for (String moduleName : appInfoSuitableOptions.getStringIterator()) {
            if (current == null) {
                // first element treated. None of them needs to point at it
                current = new TopologyElement(moduleName);
                topology.addModule(current);
            } else {
                // There were explored already other modules
                previous = current;
                current = new TopologyElement(moduleName);
                previous.addElementCalled(current);
                topology.addModule(current);
            }
        }

        return topology;
    }

    public static Map<String, Object> getAppMap() {
        return appMap;
    }

    public static void setAppMap(Map<String, Object> appMap_) {
        Parameters.appMap = appMap_;
    }

    public static SuitableOptions getAppInfoSuitableOptions() {
        return appInfoSuitableOptions;
    }

    public static void setAppInfoSuitableOptions(
            SuitableOptions appInfoSuitableOptions_) {
        Parameters.appInfoSuitableOptions = appInfoSuitableOptions_;
    }

    public static Map<String, Object> getAllCloudOffersMAP() {
        return allCloudOffersMAP;
    }

    public static void setSuitableCloudOffersMAP(
            Map<String, Object> suitableCloudOffersMAP_) {
        Parameters.allCloudOffersMAP = suitableCloudOffersMAP_;
    }

    public static String getAppModel() {
        return appModel;
    }

    public static void setAppModel(String appModel_) {
        Parameters.appModel = appModel_;
    }

    public static String getSuitableCloudOffers() {
        return suitableCloudOffers;
    }

    public static void setSuitableCloudOffers(String suitableCloudOffers_) {
        Parameters.suitableCloudOffers = suitableCloudOffers_;
    }

    public static Topology getTopology() {
        return topology;
    }

    public static void setTopology(Topology topology_) {
        Parameters.topology = topology_;
    }

}
