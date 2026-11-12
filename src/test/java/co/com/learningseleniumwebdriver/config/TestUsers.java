package co.com.learningseleniumwebdriver.config;

/**
 * Entrega las credenciales de los usuarios de prueba a partir de su alias.
 *
 * Los usuarios no se escriben en los steps ni en el código: viven en el archivo de configuración
 * de la aplicación (por ejemplo saucedemo.properties) con este formato:
 *   user.&lt;alias&gt;.name=...
 *   user.&lt;alias&gt;.password=...
 * Para cambiar un usuario o agregar otro, solo se edita ese archivo.
 */
public class TestUsers {

    private final ConfigReader config = ConfigReader.load();

    public String username(String alias) {
        return config.getRequired("user." + alias + ".name");
    }

    public String password(String alias) {
        return config.getRequired("user." + alias + ".password");
    }
}
