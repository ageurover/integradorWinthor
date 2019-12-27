package winthorDb.jpa.dao;

import oracle.toplink.essentials.internal.databaseaccess.Platform;
import oracle.toplink.essentials.internal.sessions.DatabaseSessionImpl;
import oracle.toplink.essentials.logging.JavaLog;
import oracle.toplink.essentials.logging.SessionLog;
import oracle.toplink.essentials.platform.server.ServerPlatformBase;

/**
 * TopLink Essentials ServerPlatform class which provides Java Logger only.
 * <p/>
 * This code is based from <code>NoServerPlatform</code>.
 *
 * @author Wonseok Kim
 */
public class JavaLoggerServerPlatform extends ServerPlatformBase {

    public JavaLoggerServerPlatform(DatabaseSessionImpl newDatabaseSession) {
        super(newDatabaseSession);
        this.disableRuntimeServices();
        this.disableJTA();
    }

    @Override
    public String getServerNameAndVersion() {
        return null;
    }

    @Override
    public Class getExternalTransactionControllerClass() {
        return null;
    }

    public void launchContainerThread(Thread thread) {
    }

    @Override
    public SessionLog getServerLog() {
        return new JavaLog();
    }

    @Override
    public java.sql.Connection unwrapOracleConnection(Platform platform, java.sql.Connection connection){
        return connection;
    }

}
