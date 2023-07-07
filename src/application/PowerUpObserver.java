package application;

/**
 * Observer interface
 */
public interface PowerUpObserver {
    void onPowerUpCollected(PowerUp.PwrUpType powerUpType);
}