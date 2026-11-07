##Custom AI OAuth Integration Investigator Agent

An automated security audit and technical discovery agent built to investigate public OAuth identity providers and produce real-time architectural integration profiles. 

## Architectural Design Highlights
* **Zero Overhead Framework Layer:** Implemented using Javalin for high-efficiency request routing instead of large corporate spring boot boilerplate frameworks to keep code signature unique.
* **Custom Execution Core Layer:** Uses direct internal Java HTTP Client connection streams to communicate natively with OpenAI endpoints, removing dependence on third-party SDK platforms.
* **Real-time Web Parsing Engine:** Uses Jsoup to pull and sanitize raw element structural documentation text dynamically before delivering payload components into context.

## Operational Constraints & Boundaries
* **Public Domain Access:** The discovery loop maps public-facing technical text guidelines. It does not automate logins or bypass developer portal walls to access gated administration consoles.
* **Context Budget Caps:** Documentation scraping bounds are capped to manage context latency and keep resource execution loops short.
* **Fallback Verification Mode:** Integrates an internal sandbox validation sequence triggered via `TEST_MODE` configuration inputs to evaluate engine formatting parameters.

## Local Configuration and Deployment
1. Verify Java 23 SDK environment paths are initialized on the execution machine.
2. Launch the backend system instance by executing the entry point inside `AgentApplication.java`.
3. Point any standard browser client application to `http://localhost:8080` to access the interactive audit control panel dashboard.
