package org.kobus.spring.domain.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyResvDTO {
	private String mrsMrnpNo;
    private String deprnNm;
    private String arvlNm;
    private int takeDrtm;
    private String deprnCd;
    private String arvlCd;
    private String alcnDeprnCd;
    private String alcnArvlCd;
    private String busClsCd;
    private int adltNum;
    private int chldNum;
    private int teenNum;
    private String DEPR_DT;
    private String deprTime;
    private int tissuFee;
    private String pynDvsCd;
    private String tissuStaCd;
    private String pynDtlCd;
    private String adtnPrdUseClsCd;
    private String adtnCpnNo;
    private String satsNo;
    private String mrspMbphNo;

}
