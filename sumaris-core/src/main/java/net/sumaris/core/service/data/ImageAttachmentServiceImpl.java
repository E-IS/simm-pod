package net.sumaris.core.service.data;

/*-
 * #%L
 * SUMARiS:: Core
 * %%
 * Copyright (C) 2018 SUMARiS Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.google.common.base.Preconditions;
import net.sumaris.core.dao.data.ImageAttachmentDao;
import net.sumaris.core.vo.data.ImageAttachmentVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imageAttachmentService")
public class ImageAttachmentServiceImpl implements ImageAttachmentService {

	private static final Log log = LogFactory.getLog(ImageAttachmentServiceImpl.class);

	@Autowired
	protected ImageAttachmentDao imageAttachmentDao;

	@Override
	public ImageAttachmentVO get(int id) {
		return imageAttachmentDao.get(id);
	}

	@Override
	public ImageAttachmentVO save(ImageAttachmentVO imageAttachment) {
		Preconditions.checkNotNull(imageAttachment);

		return imageAttachmentDao.save(imageAttachment);
	}

	@Override
	public void delete(int id) {
		imageAttachmentDao.delete(id);
	}

}
