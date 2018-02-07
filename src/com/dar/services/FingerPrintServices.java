package com.dar.services;

import java.security.MessageDigest;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.dar.hibernate.dao.ExchangeDAO;
import com.dar.hibernate.dao.FingerPrintDAO;
import com.dar.hibernate.entity.Exchange;
import com.dar.hibernate.entity.FingerPrint;
import com.dar.hibernate.utility.HibernateUtility;

public class FingerPrintServices {

	private static FingerPrintDAO fingerPrintDao = new FingerPrintDAO(FingerPrint.class, HibernateUtility.getSessionFactory());

	/**
	 * 
	 * @param timezone : timezone of FingerPrint of incaming user.
	 * @param resolution : revolution of FingerPrint of incaming user .
	 * @param enabledCookie : enabledcookie of FingerPrint of incaming user.
	 * @param idUser : id of incaming user.
	 * @return <code>True</code> if FingerPrint is created or updated id table, else <code>False</code>
	 */
	public static boolean create(int timezone,String resolution,int enabledCookie,int idUser) {
		List<FingerPrint> fingersPrint = fingerPrintDao.getFingersPrintByUser(idUser);		
		FingerPrint fp1 = new FingerPrint();
		fp1.setEnabledCookie(enabledCookie);
		fp1.setIdUser(idUser);
		fp1.setNbConnexion(1);
		fp1.setTimezone(timezone);
		fp1.setResolution(resolution);
		
		if(fingersPrint == null || fingersPrint.size()==0) {
			try {
				fingerPrintDao.create(fp1);
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return isExists(fp1, fingersPrint);
		}
		
	}
	
	/**
	 * This method checks if {@link FingerPrint} exists in the database.
	 *  If is not exist ,we save it in database, else we update it with increment the attribute
	 *  nBconnexion.
	 *  
	 * @param fp1 : FingerPrint recovered from header
	 * @param list : list of FingersPrint existed in database.
	 * @return <code> true</code> if fp1 is created  else <code>false</code>
	 */
	private static boolean isExists(FingerPrint fp1, List<FingerPrint> list) {
		String hashFp1 = DigestUtils.md2Hex(fp1.getTimezone()+fp1.getResolution()+fp1.getEnabledCookie());
		for(FingerPrint fp2 : list) {
			String hashFp2 = DigestUtils.md2Hex(fp2.getTimezone()+fp2.getResolution()+fp2.getEnabledCookie()); 
			if(hashFp1.equals(hashFp2)) {
				fp2.setNbConnexion(fp2.getNbConnexion()+1);
				fingerPrintDao.update(fp2);	
				return false;
			}
		}
		try {
			fingerPrintDao.create(fp1);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param id id of a current user.
	 * @return get last fingerprint of user.
	 */
	public static FingerPrint getLastFingerPrintByUser(int id) {
		return fingerPrintDao.getLastFingerPrint(id);
	}
}
