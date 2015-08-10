package com.karlnosworthy.bitbucketclient;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.karlnosworthy.bitbucketclient.model.BitBucketRepository;
import com.karlnosworthy.bitbucketclient.model.wrapper.BitBucketRepositoryWrapper;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;


public class BitBucketClient {

    public static final String LIVE_URL = "https://api.bitbucket.org/2.0";

    private static final String LOCALHOST = "127.0.0.1"; // NOPMD

    private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".store/oauth2_bitbucket");

    private SimpleDateFormat dateFormat;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private BitBucketService bitBucketServiceInstance;
    private Credential credential;





    /**
     * Authorises, initialises and returns a new BitBucketClient instance.
     *
     * @param identifier The identifier to use for OAuth authentication and FreeAgentService recognition.
     * @param secret The secret to use for OAuth authentication.
     * @return An authenticated instance which is ready to use or null
     * @throws IOException Thrown a problem is encountered during the OAuth process.
     */
    public static BitBucketClient authorise(String identifier, String secret) throws IOException {
        return authorise(identifier, secret, false);
    }

    /**
     * Authorises, initialises and returns a new BitBucketClient instance.
     *
     * @param identifier The identifier to use for OAuth authentication and FreeAgentService recognition.
     * @param secret The secret to use for OAuth authentication.
     * @param loggingEnabled Should logging be enabled.
     * @return An authenticated instance which is ready to use or null
     * @throws IOException Thrown a problem is encountered during the OAuth process.
     */
    public static BitBucketClient authorise(String identifier, String secret, boolean loggingEnabled) throws IOException {

        if (DATA_STORE_FACTORY == null) {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        }

        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken
                .authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl("https://bitbucket.org/site/oauth2/access_token"),
                new ClientParametersAuthentication(identifier, secret),
                identifier,
                "https://bitbucket.org/site/oauth2/authorize")
                .setDataStoreFactory(DATA_STORE_FACTORY).build();

        // authorize
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(LOCALHOST)
                .setPort(8080)
                .build();

        final Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        if (credential != null) {
            return new BitBucketClient(credential, LIVE_URL, loggingEnabled);
        } else {
            return null;
        }
    }


    /**
     *
     * @param owner
     * @return
     */
    public List<BitBucketRepository> getRepositories(String owner) {

        BitBucketRepositoryWrapper repositoryWrapper = bitBucketServiceInstance.getRepositories(owner);

        if (repositoryWrapper != null && repositoryWrapper.hasRepositories()) {
            return repositoryWrapper.getRepositories();
        } else {
            return null;
        }
    }

    /**
     *
     * @param owner
     * @param role
     * @return
     */
    public List<BitBucketRepository> getRepositories(String owner, BitBucketRoleType role) {
        BitBucketRepositoryWrapper repositoryWrapper = bitBucketServiceInstance.getRepositories(owner, role.getIdentifier());

        if (repositoryWrapper != null && repositoryWrapper.hasRepositories()) {
            return repositoryWrapper.getRepositories();
        } else {
            return null;
        }
    }

    private BitBucketClient(Credential oauthCredential, String apiURL, boolean loggingEnabled) {
        super();
        this.credential = oauthCredential;

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(apiURL);

        if (loggingEnabled) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        builder.setRequestInterceptor(new RequestInterceptor() {

            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Authorization", "Bearer " + credential.getAccessToken());
            }
        });

        RestAdapter restAdapter = builder.build();

        bitBucketServiceInstance = restAdapter.create(BitBucketService.class);
    }



    public enum BitBucketRoleType {

        Owner("owner"),
        Admin("admin"),
        Contributor("contributor"),
        Member("member");

        private String identifier;

        BitBucketRoleType(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    };

}
