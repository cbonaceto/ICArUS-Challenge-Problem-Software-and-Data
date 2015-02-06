function stageName = get_attack_prob_stage_name(stageType)
%GET_ATTACK_PROB_STAGE_NAME Get the attack probability stage full name.
%   Detailed explanation goes here
if strcmp(stageType, 'Pp')
    stageName = 'P(Attack|IMINT, OSINT)';
elseif strcmp(stageType, 'Ppc')
    stageName = 'P(Attack|HUMINT, IMINT, OSINT)';
elseif strcmp(stageType, 'Pt')
    stageName = 'P(Attack|SIGINT)';
elseif strcmp(stageType, 'Ptpc')
    stageName = 'P(Attack|SIGINT, HUMINT, IMINT, OSINT)';
end
end