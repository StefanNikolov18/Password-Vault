package password.vault.server.repository;

public record EnzoicPasswordResponse(
        boolean revealedInExposure,
        int relativeExposureFrequency,
        int exposureCount) {

}
