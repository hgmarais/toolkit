package com.s3.toolkit.utils;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.dialogs.IMessageProvider;

public class Utils {
	
	public static int markerSeverityToMessageType(int markerSeverity) {
		switch (markerSeverity) {
		case IMarker.SEVERITY_ERROR : return IMessageProvider.ERROR;
		case IMarker.SEVERITY_INFO : return IMessageProvider.INFORMATION;
		case IMarker.SEVERITY_WARNING : return IMessageProvider.WARNING;
		default: break;
		}
		
		return IMessageProvider.NONE;
	}

}
