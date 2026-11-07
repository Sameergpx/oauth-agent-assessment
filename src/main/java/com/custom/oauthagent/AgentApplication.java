package com.custom.oauthagent;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class AgentApplication {

    public static void main(String[] args) {
        LiveDocumentationScraper scraper = new LiveDocumentationScraper();

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/static";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(8080);

        app.post("/api/investigate", ctx -> {
            String providerName = ctx.queryParam("provider");
            String docUrl = ctx.queryParam("url");
            String userApiKey = ctx.queryParam("key");

            if (providerName == null || docUrl == null || userApiKey == null) {
                ctx.status(400).result("Missing parameters: provider, url, or key are required.");
                return;
            }

            // Test Mode Shortcut Bypass
            if ("TEST_MODE".equalsIgnoreCase(userApiKey.trim())) {
                String dynamicMockResponse = "### AI Security Audit Profile: " + providerName + "\n\n" +
                        "1. **Setup Difficulty:** Easy / Instant\n" +
                        "2. **Estimated Timeline:** 5 Minutes (Immediate Credentials Delivery)\n" +
                        "3. **Step-by-Step UI Navigation UI Steps:**\n" +
                        "   - Log into your main developer account profile dashboard.\n" +
                        "   - Navigate directly to Settings > Developer Settings > OAuth Applications.\n" +
                        "   - Click the 'Register New Application' action item control layout.\n" +
                        "   - Provide your Application Name, Homepage target URL, and secure Callback Redirect URL.\n" +
                        "   - Save settings to instantly read and export your active Client ID and unique Client Secret keys.\n" +
                        "4. **Known Integration Blockers:**\n" +
                        "   - No manual review queues or business workspace verifications are required for standard web scopes.\n" +
                        "   - Ensure your application redirection links explicitly match production callback strings to prevent flow breaks.";
                ctx.result(dynamicMockResponse);
                return;
            }

            String webContent = scraper.fetchDeveloperDocs(docUrl);
            AiInvestigatorClient investigator = new AiInvestigatorClient(userApiKey);
            String structuralGuide = investigator.analyzeProvider(providerName, webContent);

            ctx.result(structuralGuide);
        });

        app.get("/api/status", ctx -> {
            ctx.result("OAuth Investigator Agent Backend is Online and Ready.");
        });

        System.out.println("Server updated successfully on http://localhost:8080");
    }
}
