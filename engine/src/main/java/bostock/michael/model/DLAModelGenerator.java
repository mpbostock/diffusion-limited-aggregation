package bostock.michael.model;


import bostock.michael.random.Randomizer;

public interface DLAModelGenerator {
    default Particles grow(final int numParticles) {
        Particles particles = new Particles(getInitialParticles()); // Copy the initial particles to work on
        // particle count is num particles requested minus particles that are initially stuck.
        int particleCount = numParticles - particles.getNumStuckParticles();
        for (int i = 0; i < particleCount; i++) {
            Position position = getRandomPosition(particles);
            boolean stuck = hasParticleStuck(position, particles);
            while (!stuck) {
                position = move(position, particles);
                stuck = hasParticleStuck(position, particles);
            }
        }
        return particles;
    }

    default boolean hasParticleStuck(final Position position, final Particles particles) {
        boolean stuck = false;
        if (particleShouldStick(position, particles)) {
            particles.stick(position, getStuckOrder());
            stuck = true;
        }
        return stuck;
    }

    int getStuckOrder();

    default boolean particleShouldStick(final Position position, final Particles particles) {
        return particles.particleShouldStick(position);
    }

    default Position move(final Position position, final Particles particles) {
        return particles.move(position, Randomizer.randomDirection());
    }

    default Position getRandomPosition(final Particles particles) {
        return particles.getRandomPosition();
    }

    Particles getInitialParticles();
}
