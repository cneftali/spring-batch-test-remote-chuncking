package fr.cneftali.spring.batch.common.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter @Setter
public class Request implements Serializable {

	private static final long serialVersionUID = -2359721369810281691L;

	private int requestId;
	private String message;
}
