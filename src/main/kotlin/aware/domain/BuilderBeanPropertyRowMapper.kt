package aware.domain

import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.sql.ResultSet
import java.sql.SQLException

class BuilderBeanPropertyRowMapper<T, B>(mappedClass: Class<T?>, builderClass: Class<B?>) : RowMapper<T> {
    private val rowMapper: BeanPropertyRowMapper<B?>?
    private val buildMethod: Method?
    private fun validateBuilder(mappedClass: Class<T?>?, builderClass: Class<B?>?) {
        require(mappedClass!!.isAssignableFrom(buildMethod!!.returnType)) {
            String.format(
                    "Class '%s' is not a valid builder and does not produce the required response of '%s'",
                    builderClass!!.canonicalName, mappedClass.canonicalName)
        }
    }

    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNumber: Int): T? {
        val builder: B = rowMapper!!.mapRow(rs, rowNumber)
        return ReflectionUtils.invokeMethod(buildMethod!!, builder) as T?
    }

    companion object {
        private val BUILD_METHOD_NAME: String? = "build"
    }

    init {
        rowMapper = BeanPropertyRowMapper(builderClass)
        buildMethod = ReflectionUtils.findMethod(builderClass!!, BUILD_METHOD_NAME!!)
        validateBuilder(mappedClass, builderClass)
    }
}