import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1PersistentVolumeClaim;
import io.kubernetes.client.models.V1PersistentVolumeClaimList;

import java.io.IOException;

public class PVCList {
    public static void main(String[] args) throws IOException, ApiException{
        // K8S_APISERVER env var can be set by running kubectl proxy
        String apiServer = System.getenv("K8S_APISERVER");
        // namespace should default to default
        String namespace = System.getenv("K8S_NAMESPACE");

        ApiClient client = Configuration.getDefaultApiClient();
        if (apiServer == null) {
            apiServer = "https://localhost:443";
        }
        
        client.setBasePath(apiServer);
        System.out.format("%nconnecting to API server %s %n%n", apiServer);
        System.out.println("----- PVCs ----");

        Configuration.setDefaultApiClient(client);
        CoreV1Api api = new CoreV1Api(client);
        V1PersistentVolumeClaimList list = api.listPersistentVolumeClaimForAllNamespaces(null, null, null, null, null, null, null, null, null);
        for (V1PersistentVolumeClaim item : list.getItems()) {
            String name = item.getMetadata().getName();
            String volumeName = item.getSpec().getVolumeName();
            System.out.format("%-16s\t%-32s%n",name, volumeName);
        }

        System.out.println();
    }
}