import org.motechproject.nms.kilkari.domain.MctsChild;
import org.motechproject.nms.kilkari.domain.MctsMother;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPackType;
import org.motechproject.nms.kilkari.repository.MctsChildDataService;
import org.motechproject.nms.kilkari.repository.MctsMotherDataService;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdateMotherSubscriber {

    private static SubscriberDataService subscriberDataService;

    public void setSubscriberDataService(SubscriberDataService subscriberDataService) {
        this.subscriberDataService = subscriberDataService;
    }

    public static void updateSubscriber(String[] csvlineList, Map<String, List<Object>> dbList, int subscriberPurgedCount, int NotFoundCount) {

        MctsMotherDataService mctsMotherDataService = null;
        MctsChildDataService mctsChildDataService = null;

        List<Object> value = dbList.get(csvlineList[0]);  // Child BeneficiaryId
        String motherBeneficiaryId = csvlineList[1];
        // Check for motherId
        if (value != null && motherBeneficiaryId != null) {
            // check for subscriber
            Long subscriberId = (Long) value.get(0);
            if (subscriberId != null) {
                // get and check for mother in subscriber
                Subscriber subscriber = subscriberDataService.findById(subscriberId);

                MctsMother mother = mctsMotherDataService.create(new MctsMother(motherBeneficiaryId));
                MctsChild child = subscriber.getChild();
                child.setMother(mother);
                child.setDateOfBirth(subscriber.getDateOfBirth());

                if (subscriber.getMother() != null && subscriber.getMother().getBeneficiaryId() != motherBeneficiaryId) {
                    // create mother and new subscriber for child and update it in existing child subscription
                    Subscriber childSubscriber = new Subscriber(subscriber.getCallingNumber(), subscriber.getLanguage(), subscriber.getCircle());
                    childSubscriber.setMother(mother);
                    childSubscriber.setChild(child);
                    childSubscriber.setDateOfBirth(subscriber.getDateOfBirth());
                    childSubscriber = subscriberDataService.create(childSubscriber);
                    subscriber.setDateOfBirth(null);
                    Set<Subscription> subscriptions = subscriber.getAllSubscriptions();
                    for (Subscription subscription : subscriptions) {
                        if (subscription.getSubscriptionPack().getType() == SubscriptionPackType.CHILD) {
                            subscription.setSubscriber(childSubscriber);
                        }
                    }
                } else if (subscriber.getMother() == null) {
                    // Update mother in child and subscriber
                    subscriber.setMother(mother);
                }


            } else {
                // update mother in child
                MctsMother mother = mctsMotherDataService.create(new MctsMother(motherBeneficiaryId));
                MctsChild child = mctsChildDataService.findByBeneficiaryId(csvlineList[0]);
                child.setMother(mother);
                subscriberPurgedCount++;
                System.out.println("No Subscriber found for BeneficiaryId " + csvlineList[0]);
            }

        } else {
            NotFoundCount++;
            System.out.println("Child not found for BeneficiaryId " + csvlineList[0]);
        }

    }

}
