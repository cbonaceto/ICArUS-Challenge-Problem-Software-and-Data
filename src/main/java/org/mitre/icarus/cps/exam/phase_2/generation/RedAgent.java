/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.exam.phase_2.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * Generates Red actions using a Red tactics configuration.
 *
 * @author CBONACETO
 *
 */
public class RedAgent {

    /**
     * Random number generator
     */
    protected Random random;

    /**
     * Construct a new RedAgent using a random number generator seeded with 1.
     */
    public RedAgent() {
        this(new Random(1));
    }

    /**
     * Construct a new RedAgent using the given random number generator.
     *
     * @param random
     */
    public RedAgent(Random random) {
        this.random = random;
    }

    /**
     * Set a new random number generator to use.
     *
     * @param random the random number generator
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Generate a Red action probabilistically at each location given the Red
     * tactic parameters. The P and U values at each location will be used to
     * look up the probability of attack at the location using the given Red
     * tactic parameters. This probability will be multiplied by the probability
     * that Red has the capability to attack (redCapability_Pc), and the agent 
     * will select "Attack" with that probability. The agent will attack at most one location.
     *
     * @param locations the locations
     * @param redTactic the Red tactic parameters Red is currently playing with
     * @param redCapability_Pc the probability that Red has the capability to
     * attack (Pc)
     * @return the Red action at each location
     */
    public RedAction selectRedAction(List<BlueLocation> locations, RedTacticParameters redTactic,
            Double redCapability_Pc) {
        if (random == null) {
            random = new Random(1);
        }
        if (redTactic != null && locations != null && !locations.isEmpty()) {
            RedActionType actionType = null;
            List<Integer> locationIndexes = createLocationIndexes(locations);
            int locationIndex = 0;
            BlueLocation location = null;
            Iterator<Integer> locationIndexIter = locationIndexes.iterator();
            while (locationIndexIter.hasNext() && actionType != RedActionType.Attack) {
                locationIndex = locationIndexIter.next();
                location = locations.get(locationIndex);

                //Get the probability of attack based on the values of P and U at the location (P|OSINT, IMINT))
                Double attackProbability = redTactic.getAttackProbability(
                        location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                        location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0);
                if (redCapability_Pc != null) {
                    //Multiply the probability by whether Red has the capability to attack to get the overall probability of attack
                    attackProbability *= redCapability_Pc;
                }

                //Select a Red action based on the probability of attack
                if (attackProbability >= random.nextDouble()) {
                    //Attack
                    actionType = RedActionType.Attack;
                }
				//DEBUG CODE
                //System.out.println("P(Activity, Capability, Propensity): " + attackProbability + ", Red Action: " + (actionType != null ? actionType : RedActionType.Do_Not_Attack));
                //END DEBUG CODE
            }
            return new RedAction(location != null ? location.getId() : null, locationIndex,
                    actionType == null ? RedActionType.Do_Not_Attack : actionType);
        }
        return null;
    }

    /**
     * Generates a list containing the numbers [0, 1, ...locations.size-1] and
     * shuffles the list using the random number generator.
     *
     * @param locations the locations
     * @return a shuffled list containing the numbers [0, 1,...locations.size-1]
     */
    protected List<Integer> createLocationIndexes(List<BlueLocation> locations) {
        ArrayList<Integer> indexes = null;
        if (locations != null && !locations.isEmpty()) {
            indexes = new ArrayList<Integer>(locations.size());
            for (int i = 0; i < locations.size(); i++) {
                indexes.add(i);
            }
            Collections.shuffle(indexes, random);
        }
        return indexes;
    }
}
