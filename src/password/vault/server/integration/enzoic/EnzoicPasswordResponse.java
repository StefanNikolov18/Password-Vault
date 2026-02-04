package password.vault.server.integration.enzoic;

public record EnzoicPasswordResponse(
        boolean revealedInExposure,
        int relativeExposureFrequency,
        int exposureCount) {

}
