package org.jboss.as.quickstart.hibernate4.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;
import org.jboss.as.quickstart.hibernate4.model.EmailAddress;

public class EmailAddressType implements UserType {

	@Override
	public Class<?> returnedClass() {
		return EmailAddress.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { StringType.INSTANCE.sqlType() };
	}

	@Override
	public EmailAddress nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException,
			SQLException {
		return EmailAddress.valueOf(rs.getString(names[0]));
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if(value == null)
			StringType.INSTANCE.set(st, null, index, session);
		else
			StringType.INSTANCE.set(st, ((EmailAddress)value).getValue(), index, session);
	}

	@Override
	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		return arg0.equals(arg1);
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object deepCopy(Object arg0) throws HibernateException {
		return arg0 == null ? null : EmailAddress.valueOf(((EmailAddress)arg0).getValue());
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		throw new UnsupportedOperationException();
	}

}
